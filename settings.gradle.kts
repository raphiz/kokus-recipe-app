rootProject.name = "kokus"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    includeBuild("gradle/plugins")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":domain:meal-plan")
include(":domain:recipe-collection")
include(":domain:shopping-list")
include(":infrastructure:app")
include(":infrastructure:base")
