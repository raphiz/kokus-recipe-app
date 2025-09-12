plugins {
    id("li.raphael.kokus.root")
    id("li.raphael.kokus.c4.diagrams")
}
//
// c4Diagrams {
//    title = "Kokus"
//    description =
//        """
//        Enables households to collect and manage recipes, plan meals, and maintain shared shopping lists
//        """.trimIndent()
//    technology = "Kotlin / JVM"
//    relations {
//        incoming(
//            from = Persons.householdMember,
//            description = "collects recipes, plans meals, and maintains a shared shopping list in",
//            protocol = "HTTP",
//        )
//    }
// }

val taskGroup = "Kokus"

// Defines high-level lifecycle tasks for this multi-project build.
// These serve as entry points for common dev and CI workflows.
tasks.register("run").configure {
    group = taskGroup
    description = "Run the application locally for manual review and testing"
    dependsOn(":app:assembly:run")
}

tasks.named("assemble").configure {
    group = taskGroup
    description = "Assemble application distribution artifacts for deployment"
    dependsOn(":app:assembly:installDist")
}

tasks.named("check").configure {
    group = taskGroup
    description = "Run all tests"

    val checkTasks =
        subprojects
            .filter { it.buildFile.isFile } // skip synthetic parent projects
            .map { "${it.path}:check" }
    dependsOn(checkTasks)
}

tasks.named("clean") {
    group = taskGroup
    description = "Clean all projects"

    val cleanTasks =
        subprojects
            .filter { it.buildFile.isFile } // skip synthetic parent projects
            .map { "${it.path}:clean" }
    dependsOn(cleanTasks)
}

// Customize the "tasks" task to only show these lifecycle tasks
tasks.named<TaskReportTask>("tasks") {
    displayGroup = taskGroup
}
