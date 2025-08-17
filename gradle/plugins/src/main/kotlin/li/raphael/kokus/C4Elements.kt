package li.raphael.kokus

import li.raphael.kokus.c4.core.C4Element

object Persons {
    val householdMember =
        C4Element.Person(
            name = "Household Member",
            description = "Collaborates with others by planning meals, contributing recipes, and maintaining a shared shopping list",
        )
}

object SoftwareSystems {
    val cookingWebsite =
        C4Element.SoftwareSystem(
            name = "Cooking Website",
            description = "Provides accessible recipe data that can be imported into the application",
        )
}
