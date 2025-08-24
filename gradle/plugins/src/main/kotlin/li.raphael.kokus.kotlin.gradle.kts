plugins {
    kotlin("jvm")
}

kotlin {
    compilerOptions {
        jvmToolchain(21)
        freeCompilerArgs.set(listOf("-Xjsr305=strict"))
    }
}

val Project.libs get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

operator fun VersionCatalog.get(name: String) = findLibrary(name).get()

dependencies {
    testImplementation(platform(libs["junit-bom"]))
    testImplementation(libs["junit-jupiter-api"])
    testRuntimeOnly(libs["junit-platform-launcher"])
    testRuntimeOnly(libs["junit-jupiter-engine"])
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
