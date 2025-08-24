import li.raphael.kokus.invoke
import li.raphael.kokus.libs

plugins {
    kotlin("jvm")
}

kotlin {
    compilerOptions {
        jvmToolchain(21)
        freeCompilerArgs.set(listOf("-Xjsr305=strict"))
        allWarningsAsErrors = true
    }
}

dependencies {
    testImplementation(platform(libs("junit-bom")))
    testImplementation(libs("junit-jupiter-api"))
    testRuntimeOnly(libs("junit-platform-launcher"))
    testRuntimeOnly(libs("junit-jupiter-engine"))
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
