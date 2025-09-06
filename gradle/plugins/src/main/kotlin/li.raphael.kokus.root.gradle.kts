plugins {
    id("li.raphael.kokus.base")
    id("io.github.pemistahl.version-catalog-linter")
    kotlin("jvm") apply false
}

versionCatalogLinter {
    bomsAndDependencies.put(
        "junit-bom",
        listOf("junit-jupiter-api", "junit-platform-launcher", "junit-jupiter-engine"),
    )
}

tasks.named("check") {
    dependsOn(tasks.named("checkVersionCatalog"))
}
