import li.raphael.kokus.CheckFilesCleanTask
import li.raphael.kokus.c4.core.DiagramType
import li.raphael.kokus.c4.gradle.C4DiagramsExtension
import li.raphael.kokus.c4.gradle.registerRenderDiagramTask

require(project.rootProject == project) { "This plugin must be applied to the root project" }

val c4DiagramsExtension =
    extensions.create("c4Diagrams", C4DiagramsExtension::class.java).apply {
        title.convention(project.name)
        description.convention("")
    }

// register specific c4 level 1 & level 2 tasks
val diagramTasks =
    arrayOf(
        tasks.registerRenderDiagramTask(DiagramType.CONTAINER, c4DiagramsExtension),
        tasks.registerRenderDiagramTask(DiagramType.SYSTEM_CONTEXT, c4DiagramsExtension),
    )

// register lifecycle task
tasks.register("renderDiagrams").configure {
    dependsOn(diagramTasks)
}

val checkDiagramsUpToDate by tasks.registering(CheckFilesCleanTask::class) {
    targetFiles.from(diagramTasks.map { provider -> provider.map { it.outputFile } })
}
tasks.named("check").configure {
    dependsOn(checkDiagramsUpToDate)
}
