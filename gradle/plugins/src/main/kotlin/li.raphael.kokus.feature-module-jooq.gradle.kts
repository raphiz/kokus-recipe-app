import li.raphael.kokus.facet
import li.raphael.kokus.registeringFacet

plugins {
    id("li.raphael.kokus.module")
}

val db by registeringFacet()

dependencies {
    db.implementationConfigurationName(facet(project(path), "domain"))

    db.implementationConfigurationName(facet(project(":app:fundamentals:database"), "jooq"))
}
