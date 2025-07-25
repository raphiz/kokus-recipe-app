
// Produce reproducible archives, see https://github.com/raphiz/buildGradleApplication?tab=readme-ov-file#rule-6-tell-gradle-to-be-more-reproducible
tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    dirPermissions { unix("755") }
    filePermissions { unix("644") }
}

// Fail build when dynamic dependency versions or Changing versions (SNAPSHOTS, etc) are used
configurations.configureEach {
    resolutionStrategy {
        failOnNonReproducibleResolution()
    }
}
