plugins {
    kotlin("jvm")
}

kotlin {
    compilerOptions {
        jvmToolchain(21)
        freeCompilerArgs.set(listOf("-Xjsr305=strict"))
    }
}
