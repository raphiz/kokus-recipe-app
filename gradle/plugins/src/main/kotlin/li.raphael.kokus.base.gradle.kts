plugins {
    idea
}

// Fail build when dynamic dependency versions or Changing versions (SNAPSHOTS, etc) are used
configurations.configureEach {
    resolutionStrategy {
        failOnNonReproducibleResolution()
    }
}

tasks.register("eagerCanary") {
    group = "verification"
    description =
        """
        if this configuration is executed, an eager API is used
        see https://docs.gradle.org/current/userguide/task_configuration_avoidance.html#eager_apis_to_avoid        
        """.trimIndent()

    if (gradle.startParameter.taskNames.none { it == "tasks" || it.endsWith(":tasks") }) {
        throw IllegalStateException(
            """
            Eager task configuration detected: All tasks were realized during configuration phase.
            To avoid this, use lazy configuration APIs.
            See https://docs.gradle.org/current/userguide/task_configuration_avoidance.html#eager_apis_to_avoid for details.
            """.trimIndent(),
        )
    }
}

idea {
    module {
        excludeDirs.addAll(
            setOf(
                ".gradle",
                ".pre-commit-config.yaml",
                "result",
                ".idea",
                ".kotlin",
                "build",
                "out",
            ).map { file(it) },
        )
    }
}
