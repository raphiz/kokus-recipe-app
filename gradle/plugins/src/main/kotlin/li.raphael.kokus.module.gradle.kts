plugins {
    kotlin("jvm")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.set(listOf("-Xjsr305=strict"))
        jvmToolchain(21)
    }
}

// Produce reproducible archives, see https://github.com/raphiz/buildGradleApplication?tab=readme-ov-file#rule-6-tell-gradle-to-be-more-reproducible
tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    dirPermissions { unix("755") }
    filePermissions { unix("644") }
}

configurations.configureEach {
    resolutionStrategy {
        failOnNonReproducibleResolution()
    }
}
