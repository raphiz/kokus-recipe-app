// Fail build when dynamic dependency versions or Changing versions (SNAPSHOTS, etc) are used
configurations.configureEach {
    resolutionStrategy {
        failOnNonReproducibleResolution()
    }
}
