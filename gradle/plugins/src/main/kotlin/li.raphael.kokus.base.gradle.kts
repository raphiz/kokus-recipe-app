plugins {
    idea
}

@CacheableTask
abstract class WriteModuleDepsTask
    @Inject
    constructor(
        objects: ObjectFactory,
    ) : DefaultTask() {
        @get:Input
        abstract val projectPath: ListProperty<String>

        @get:Input
        abstract val projectDependencies: ListProperty<String>

        @get:Input
        abstract val externalDependencies: ListProperty<String>

        @get:OutputFile
        abstract val outputFile: RegularFileProperty

        @TaskAction
        fun write() {
            val json =
                buildString {
                    append("{\"path\":")
                    append(escape(projectPath.get().single()))
                    append(",\"projectDependencies\":[")
                    append(projectDependencies.get().joinToString(",") { escape(it) })
                    append("],\"externalDependencies\":[")
                    append(externalDependencies.get().joinToString(",") { escape(it) })
                    append("]}")
                }
            outputFile.get().asFile.also { file ->
                file.parentFile.mkdirs()
                file.writeText(json)
            }
        }

        private fun escape(s: String) = "\"" + s.replace("\"", "\\\"") + "\""
    }

val writeModuleDeps by tasks.registering(WriteModuleDepsTask::class) {
    projectDependencies.set(
        project.configurations
            .matching { it.isCanBeResolved || it.isCanBeConsumed || it.isCanBeDeclared } // collect broadly
            .flatMap { cfg -> cfg.dependencies.withType(ProjectDependency::class.java) }
            .map { it.path } // SAFE: does not touch the target project's model on Gradle 8.11+
            .distinct()
            .sorted(),
    )
    // TODO: these contain ALL transitive dependencies - maybe also add a variant with only direct dependencies?
    // TODO: Also set these by scope (`api`, `implementation` etc. - flat like this does not really make sense
    externalDependencies.set(
        project.configurations
            .matching { it.isCanBeResolved || it.isCanBeConsumed || it.isCanBeDeclared } // collect broadly
            .flatMap { cfg -> cfg.dependencies.withType(ExternalDependency::class.java) }
            .map { it.module.toString() }
            .distinct()
            .sorted(),
    )
    projectPath.set(listOf(project.path))
    outputFile.set(layout.buildDirectory.file("reports/module-deps.json"))
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
