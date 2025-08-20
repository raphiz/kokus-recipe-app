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

include(":subsystems:composition")
include(":subsystems:meal-plan")
include(":subsystems:recipe-collection")
include(":subsystems:shopping-list")
include(":infrastructure:base")
