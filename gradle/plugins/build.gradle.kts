plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(
        libs.plugins.kotlin.jvm
            .asDependency(),
    )

    implementation(libs.structurizr.client)
    implementation(libs.structurizr.autolayout)
    implementation(libs.structurizr.export)
    implementation(libs.plantuml)
}

tasks.validatePlugins.configure {
    enableStricterValidation = true
}

fun Provider<PluginDependency>.asDependency(): String =
    map {
        "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
    }.get()
