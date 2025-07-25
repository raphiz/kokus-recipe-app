plugins {
    id("li.raphael.kokus.module")
}

dependencies {
    implementation(projects.domain.recipeCollection)

    implementation(projects.infrastructure.base)
}
