import li.raphael.kokus.feature

plugins {
    id("li.raphael.kokus.module")
}

val domain = sourceSets.maybeCreate("domain")
java.registerFeature(domain.name) {
    usingSourceSet(domain)
}

dependencies {
    implementation(feature(project(path), domain.name))
    domain.apiConfigurationName(project(":app:fundamentals"))
}
