plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(pluginMarker(libs.plugins.kotlin.jvm))

    implementation(libs.structurizr.client)
    implementation(libs.structurizr.autolayout)
    implementation(libs.structurizr.export)
    implementation(libs.plantuml)
}

tasks.validatePlugins.configure {
    enableStricterValidation = true
}

fun pluginMarker(provider: Provider<PluginDependency>): String {
    val plugin = provider.get()
    return "${plugin.pluginId}:${plugin.pluginId}.gradle.plugin:${plugin.version}"
}
