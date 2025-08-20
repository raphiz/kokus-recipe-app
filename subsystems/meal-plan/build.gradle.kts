import li.raphael.kokus.Persons
import li.raphael.kokus.feature

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
    implementation(feature(project(":subsystems:recipe-collection"), "domain")) {
        because("uses recipes from")
    }
}
