pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    // Sadly, it's not posisble to use a plugin declared in a version catalog in a settings file
    id("dev.panuszewski.typesafe-conventions") version "0.7.4"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

rootProject.name = "kokus-plugins"
