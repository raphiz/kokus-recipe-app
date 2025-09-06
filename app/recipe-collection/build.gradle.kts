import li.raphael.kokus.Persons
import li.raphael.kokus.SoftwareSystems

plugins {
    id("li.raphael.kokus.feature-module")
    id("li.raphael.kokus.feature-module-jooq")
}

c4Container {
    name = "Recipe Collection"
    description =
        """
        Provides a curated collection of recipes with detailed ingredients, instructions, and categorizations
        """.trimIndent()

    relations {
        incoming(
            from = Persons.householdMember,
            description = "creates, imports, modifies, and selects recipes in",
            protocol = "HTTP",
        )
        outgoing(
            to = SoftwareSystems.cookingWebsite,
            description = "imports recipes from",
            protocol = "HTTP",
        )
    }
}
