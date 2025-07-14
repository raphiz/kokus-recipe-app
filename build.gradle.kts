plugins {
    kotlin("jvm") version "2.1.0"
    application
}

application {
    mainClass.set("com.example.MainKt")
}

group = "com.example"
version = System.getenv("APP_VERSION") ?: "dirty"

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))

    // Junit
    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("io.strikt:strikt-core:0.35.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        freeCompilerArgs.set(listOf("-Xjsr305=strict"))
        jvmToolchain(21)
    }
}

tasks.named<JavaExec>("run").configure {
    jvmArgs("-XX:TieredStopAtLevel=1")
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    dirPermissions { unix("755") }
    filePermissions { unix("644") }
}

configurations.all {
    resolutionStrategy {
        failOnNonReproducibleResolution()
    }
}
