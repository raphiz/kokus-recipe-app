import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(pluginMarker(libs.plugins.versionCatalogLinter))
    implementation(pluginMarker(libs.plugins.kotlin.jvm))

    implementation(pluginMarker(libs.plugins.jooq.codegen))
    implementation(libs.jooq.meta)

    implementation(libs.flyway.core)
    runtimeOnly(libs.flyway.postgresql)
    runtimeOnly(libs.postgresql)

    implementation(libs.structurizr.client)
    implementation(libs.structurizr.autolayout)
    implementation(libs.structurizr.export)
    implementation(libs.plantuml)
}

tasks.validatePlugins.configure {
    enableStricterValidation = true
}

kotlin {
    compilerOptions {
        jvmTarget.closestSupportedTo(25)
        freeCompilerArgs.set(listOf("-Xjsr305=strict"))
        allWarningsAsErrors = true
    }
}

// Ensures Kotlin uses a JVM target it actually supports by falling back to the closest supported one.
fun Property<JvmTarget>.closestSupportedTo(requested: Int) =
    set(
        JvmTarget.entries.last { it.target.substringBefore(".").toInt() <= requested },
    )

tasks.withType<KotlinJvmCompile>().configureEach {
    // This is only a good idea for Kotlin-only projects.
    // See https://kotlinlang.org/docs/gradle-configure-project.html#what-can-go-wrong-if-targets-are-incompatible
    jvmTargetValidationMode.set(JvmTargetValidationMode.IGNORE)
}

fun pluginMarker(provider: Provider<PluginDependency>): String {
    val plugin = provider.get()
    return "${plugin.pluginId}:${plugin.pluginId}.gradle.plugin:${plugin.version}"
}
