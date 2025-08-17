plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.2.0")

    implementation("com.structurizr:structurizr-client:4.1.0")
    implementation("com.structurizr:structurizr-autolayout:4.1.0")
    implementation("com.structurizr:structurizr-export:4.1.0")
    implementation("net.sourceforge.plantuml:plantuml:1.2025.4")
}

tasks.validatePlugins.configure {
    enableStricterValidation = true
}
