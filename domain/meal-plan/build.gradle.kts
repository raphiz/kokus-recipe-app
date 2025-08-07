import li.raphael.kokus.feature

plugins {
    id("li.raphael.kokus.feature-module")
}

dependencies {
    domainApi(feature(project(":domain:recipe-collection"), "domain"))
}
