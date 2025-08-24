import li.raphael.kokus.facet
import li.raphael.kokus.registeringFacet

plugins {
    id("li.raphael.kokus.module")
}

val domain by registeringFacet()

dependencies {
    implementation(facet(project(path), domain.name))
    domain.apiConfigurationName(project(":app:fundamentals"))
}
