plugins {
    id("li.raphael.kokus.root")
}

val taskGroup = "Kokus"

// Defines high-level lifecycle tasks for this multi-project build.
// These serve as entry points for common dev and CI workflows.
tasks.register("run").configure {
    group = taskGroup
    description = "Run the application locally for manual review and testing"
    dependsOn(project(":infrastructure:app").tasks.named("run"))
}

tasks.named("assemble").configure {
    group = taskGroup
    description = "Assemble application distribution artifacts for deployment"
    dependsOn(project(":infrastructure:app").tasks.named("installDist"))
}

tasks.named("check").configure {
    group = taskGroup
    description = "Run all tests"

    // Safely aggregate existing 'check' tasks from subprojects skipping projects without one
    val checkTasks =
        subprojects.mapNotNull {
            runCatching { it.tasks.named("check") }.getOrNull()
        }
    dependsOn(checkTasks)
}

tasks.named("clean") {
    group = taskGroup
    description = "Clean all projects"

    // Safely aggregate existing 'clean' tasks from subprojects skipping projects without one
    val cleanTasks =
        subprojects.mapNotNull {
            runCatching { it.tasks.named("clean") }.getOrNull()
        }
    dependsOn(cleanTasks)
}

// Customize the "tasks" task to only show these lifecycle tasks
tasks.named<TaskReportTask>("tasks") {
    displayGroup = taskGroup
}
