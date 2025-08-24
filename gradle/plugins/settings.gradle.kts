pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

rootProject.name = "kokus-plugins"
