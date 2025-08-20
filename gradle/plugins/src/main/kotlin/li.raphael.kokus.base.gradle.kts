plugins {
    idea
}

// Fail build when dynamic dependency versions or Changing versions (SNAPSHOTS, etc) are used
configurations.configureEach {
    resolutionStrategy {
        failOnNonReproducibleResolution()
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
