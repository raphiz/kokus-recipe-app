package li.raphael.kokus.c4.gradle

import li.raphael.kokus.c4.core.*
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.gradle.kotlin.dsl.withType

@CacheableTask
abstract class RenderDiagramTask : DefaultTask() {
    @get:Input
    abstract val modules: ListProperty<C4Project>

    @get:Input
    abstract val title: Property<String>

    @get:Input
    abstract val systemDescription: Property<String>

    @get:Input
    abstract val technology: Property<String>

    @get:Input
    abstract val tags: ListProperty<String>

    @get:Input
    internal abstract val incomingRelations: ListProperty<C4ContainerRelation>

    @get:Input
    internal abstract val outgoingRelations: ListProperty<C4ContainerRelation>

    @get:Input
    abstract val diagramType: Property<DiagramType>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun generate() {
        outputFile.asFile.get().outputStream().use { output ->
            val view =
                buildViewFor(
                    C4Project(
                        projectId = ProjectId(":"),
                        name = title.get(),
                        description = systemDescription.get(),
                        technology = technology.get(),
                        tags = tags.get().toSortedSet(),
                        dependencies = sortedSetOf(),
                        incomingRelations = incomingRelations.get().toSortedSet(),
                        outgoingRelations = outgoingRelations.get().toSortedSet(),
                        hidden = false,
                    ),
                    unfilteredProjects = modules.get(),
                    diagramType = diagramType.get(),
                )
            PlantUmlRenderer().render(view, output)
        }
    }
}

fun TaskContainer.registerRenderDiagramTask(
    type: DiagramType,
    c4DiagramsExtension: C4DiagramsExtension,
): TaskProvider<RenderDiagramTask> {
    val taskName = "render${type.title.toPascalCase()}Diagram"
    return register<RenderDiagramTask>(taskName) {
        group = "documentation"
        title.convention(c4DiagramsExtension.title)
        systemDescription.convention(c4DiagramsExtension.description)
        technology.convention(c4DiagramsExtension.technology)
        tags.convention(c4DiagramsExtension.tags)
        incomingRelations.convention(c4DiagramsExtension.incomingRelations)
        outgoingRelations.convention(c4DiagramsExtension.outgoingRelations)
        diagramType.set(type)
        modules.set(
            project.provider {
                project.subprojects.mapNotNull { it.toModuleInfo() }
            },
        )
        outputFile.set(project.layout.projectDirectory.file("c4-${type.title.toKebabCase()}-diagram.svg"))
    }
}

private fun Project.toModuleInfo(): C4Project? {
    val extension = extensions.findByType(C4ContainerExtension::class.java)
    return if (extension == null) {
        null
    } else {
        C4Project(
            ProjectId(path),
            extension.name.get(),
            extension.description.get { "Missing c4 container description for $path" },
            extension.technology.get(),
            extension.tags.get().toSortedSet(),
            containerDependencies.toSortedSet(),
            extension.incomingRelations.get().toSortedSet(),
            extension.outgoingRelations.get().toSortedSet(),
            extension.hidden.get(),
        )
    }
}

private val Project.containerDependencies: Set<C4ContainerDependency>
    get() =
        configurations
            .flatMap { configuration ->
                configuration.dependencies
                    .withType<ProjectDependency>()
                    .filterNot { it.path == this.path } // ignore self references (eg. for feature variants)
                    .map { projectDependency ->
                        C4ContainerDependency(
                            projectId = ProjectId(projectDependency.path),
                            scope = configuration.name,
                            description = projectDependency.reason,
                            technology = projectDependency.getTechnology(),
                        )
                    }
            }.toSet()

private fun <T : Any> Property<T>.get(lazyMessage: () -> String): T =
    if (!this.isPresent) {
        throw GradleException(lazyMessage())
    } else {
        this.get()
    }

private fun String.toPascalCase() = split(" ").joinToString("") { part -> part.uppercaseFirstChar() }

private fun String.toKebabCase() = split(" ").joinToString("-") { part -> part.lowercase() }
