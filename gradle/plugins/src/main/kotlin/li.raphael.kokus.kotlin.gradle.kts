import li.raphael.kokus.invoke
import li.raphael.kokus.libs
import li.raphael.kokus.maxSupportedUpTo
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm")
}

val jvmTargetVersion = JvmTarget.maxSupportedUpTo(25)

kotlin {
    compilerOptions {
        jvmTarget.set(jvmTargetVersion)
        freeCompilerArgs.set(listOf("-Xjsr305=strict"))
        allWarningsAsErrors = true
    }
}

java {
    val targetJavaVersion = JavaVersion.toVersion(jvmTargetVersion.target)
    sourceCompatibility = targetJavaVersion
    targetCompatibility = targetJavaVersion
}

tasks.withType<KotlinJvmCompile>().configureEach {
    // This is only a good idea for Kotlin-only projects.
    // See https://kotlinlang.org/docs/gradle-configure-project.html#what-can-go-wrong-if-targets-are-incompatible
    jvmTargetValidationMode.set(JvmTargetValidationMode.IGNORE)
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
