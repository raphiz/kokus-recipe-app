plugins {
    kotlin("jvm")
}

kotlin {
    compilerOptions {
        jvmToolchain(21)
        freeCompilerArgs.set(listOf("-Xjsr305=strict"))
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
