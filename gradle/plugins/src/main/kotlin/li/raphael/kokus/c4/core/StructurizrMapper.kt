@file:Suppress("TooManyFunctions")

package li.raphael.kokus.c4.core

import com.structurizr.Workspace
import com.structurizr.model.*
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.Shape
import com.structurizr.view.StaticView

internal fun buildViewFor(
    rootProject: C4Project,
    unfilteredProjects: List<C4Project>,
    diagramType: DiagramType,
): StaticView {
    val projects = unfilteredProjects.filterNot { it.hidden }

    val workspace =
        buildWorkspace(
            systemName = rootProject.name,
            description = rootProject.description,
        ) {
            addExternalElements(projects)
            addContainers(projects)
            addRelations(rootProject, projects)
        }

    return workspace.createViewFor(diagramType)
}

private fun buildWorkspace(
    systemName: String,
    description: String,
    initializer: Workspace.() -> Unit,
): Workspace =
    Workspace(systemName, description).apply {
        model.impliedRelationshipsStrategy = CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy()
        model.addSoftwareSystem(systemName, description)
        initializer()
    }

// The first registered software system is our main software system
private val Workspace.softwareSystem
    get() =
        requireNotNull(model.softwareSystems.firstOrNull()) {
            "SoftwareSystem must exist in Workspace before adding containers."
        }

private fun Workspace.addExternalElements(projects: List<C4Project>) {
    projects
        .flatMap { it.outgoingRelations + it.incomingRelations }
        .map { it.target }
        .distinctBy { it.name }
        .forEach { element ->
            when (element) {
                is C4Element.Person -> {
                    if (model.getPersonWithName(element.name) == null) {
                        model.addPerson(element.name, element.description)
                    }
                }

                is C4Element.SoftwareSystem -> {
                    if (model.getSoftwareSystemWithName(element.name) == null) {
                        model.addSoftwareSystem(element.name, element.description)
                    }
                }
            }
        }
}

private fun Workspace.addContainers(projects: List<C4Project>) {
    projects.forEach { project ->
        val container = softwareSystem.addContainer(project.name, project.description, project.technology)
        project.tags.forEach { tag -> container.addTags(tag) }
    }
}

private fun Workspace.addRelations(
    rootProject: C4Project,
    projects: List<C4Project>,
) {
    addExternalSystemContextRelations(projects, rootProject)

    projects.forEach { project ->
        val container =
            softwareSystem.getContainerWithName(project.name)
                ?: error("Container '${project.name}' not found for project ${project.projectId.path}")

        container.addContainerDependencies(project, projects)
        container.addIncomingRelations(project)
        container.addOutgoingRelations(project)
    }
}

private fun Workspace.addExternalSystemContextRelations(
    projects: List<C4Project>,
    rootProject: C4Project,
) {
    softwareSystem.addIncomingRelations(rootProject)
    softwareSystem.addOutgoingRelations(rootProject)

    val containerRelationsByExternalElement =
        projects
            .flatMap { project -> project.incomingRelations }
            .groupBy { relation -> relation.target.name }
            .mapValues { (_, relations) -> relations.toSet() }

    val explicitSystemLevelOverrides =
        rootProject.incomingRelations
            .map { it.target.name }
            .toSet()

    containerRelationsByExternalElement
        .filterNot { (elementName, _) -> elementName in explicitSystemLevelOverrides }
        .forEach { (elementName, relations) ->
            val externalElement =
                when (relations.first().target) {
                    is C4Element.Person -> model.getPersonWithName(elementName)
                    is C4Element.SoftwareSystem -> model.getSoftwareSystemWithName(elementName)
                }

            externalElement?.let { element ->
                relations.forEach { relation ->
                    element.uses(softwareSystem, relation.description, relation.technology)
                }
            }
        }
}

// Container-to-container dependencies within the same software system
private fun Container.addContainerDependencies(
    project: C4Project,
    projects: List<C4Project>,
) {
    project.dependencies.forEach { dependency ->
        val dependencyProject = projects.find { it.projectId == dependency.projectId }
        if (dependencyProject != null) {
            val target =
                softwareSystem.getContainerWithName(dependencyProject.name)
                    ?: error("Target container '${dependencyProject.name}' not found for dependency from '${project.name}'")
            this.uses(
                target,
                dependency.description ?: "uses",
                dependency.technology.orEmpty(),
            )
        }
    }
}

private fun StaticStructureElement.addIncomingRelations(project: C4Project) {
    project.incomingRelations.forEach { rel ->
        when (val target = rel.target) {
            is C4Element.Person -> {
                val person: Person =
                    model.getPersonWithName(target.name)
                        ?: error("Person '${target.name}' not found for incoming relation to '${project.name}'")
                person.uses(this, rel.description, rel.technology)
            }

            is C4Element.SoftwareSystem -> {
                val sws =
                    model.getSoftwareSystemWithName(target.name)
                        ?: error("Software system '${target.name}' not found for incoming relation to '${project.name}'")
                sws.uses(this, rel.description, rel.technology)
            }
        }
    }
}

fun StaticStructureElement.uses(
    element: StaticStructureElement,
    description: String,
    technology: String,
): Relationship? =
    when (element) {
        is Container -> uses(element, description, technology)
        is SoftwareSystem -> uses(element, description, technology)
        is Person -> uses(element, description, technology)
        else -> throw NotImplementedError("Relation support for ${element::class.simpleName} not yet implemented")
    }

private fun StaticStructureElement.addOutgoingRelations(project: C4Project) {
    project.outgoingRelations.forEach { relation ->
        when (val target = relation.target) {
            is C4Element.SoftwareSystem -> {
                val targetSystem =
                    model.getSoftwareSystemWithName(target.name)
                        ?: error("Target software system '${target.name}' not found for outgoing relation from '${project.name}'")
                this.uses(targetSystem, relation.description, relation.technology)
            }

            is C4Element.Person -> {
                error("An outgoing relation from container to person is not allowed")
            }
        }
    }
}

private fun Workspace.createViewFor(diagramType: DiagramType): StaticView {
    val view: StaticView =
        when (diagramType) {
            DiagramType.CONTAINER -> {
                views
                    .createContainerView(
                        softwareSystem,
                        diagramType.name,
                        diagramType.title,
                    ).apply {
                        addAllContainers()
                    }
            }

            DiagramType.SYSTEM_CONTEXT -> {
                views
                    .createSystemContextView(
                        softwareSystem,
                        diagramType.name,
                        diagramType.title,
                    ).apply {
                        enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight)
                    }
            }
        }

    view.addAllPeople()
    view.addAllSoftwareSystems()
    view.viewSet.configuration.styles
        .addElementStyle("Database")
        .shape(Shape.Cylinder)
        .background("#ffffff")

    return view
}
