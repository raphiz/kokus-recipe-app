import li.raphael.kokus.facet
import li.raphael.kokus.registerFacet

plugins {
    id("li.raphael.kokus.module")
}

val domain = registerFacet("domain")

dependencies {
    implementation(facet(project(path), domain.name))
    domain.apiConfigurationName(project(":app:fundamentals"))
}
