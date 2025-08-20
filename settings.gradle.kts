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

include(":app:assembly")
include(":app:fundamentals")
include(":app:meal-plan")
include(":app:recipe-collection")
include(":app:shopping-list")
