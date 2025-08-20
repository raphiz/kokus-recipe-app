import li.raphael.kokus.Persons
import li.raphael.kokus.feature

plugins {
    id("li.raphael.kokus.feature-module")
}

c4Container {
    description = "Generates shopping lists from planned meals, consolidating required ingredients with quantities"
    relations {
        incoming(
            Persons.householdMember,
            "reviews, edits, and checks off items in",
            "HTTP",
        )
    }
}

dependencies {
    implementation(feature(projects.app.mealPlan, "domain")) {
        because("derives ingredients from")
    }
}
