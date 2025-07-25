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

include(":domain:meal-plan")
include(":domain:recipe-collection")
include(":domain:shopping-list")
include(":infrastructure:app")
