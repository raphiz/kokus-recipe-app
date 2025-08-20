import li.raphael.kokus.Persons
import li.raphael.kokus.facet

plugins {
    id("li.raphael.kokus.feature-module")
}

c4Container {
    name = "Meal Plan"
    description = "Plans meals by combining recipes into personalized schedules"
    relations {
        incoming(
            Persons.householdMember,
            "creates and adjusts meal plans using",
            "HTTP",
        )
    }
}

dependencies {
    implementation(facet(projects.app.recipeCollection, "domain")) {
        because("uses recipes from")
    }
}
