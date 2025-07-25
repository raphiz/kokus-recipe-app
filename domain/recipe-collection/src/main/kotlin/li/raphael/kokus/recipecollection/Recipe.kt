package li.raphael.kokus.recipecollection

import li.raphael.kokus.base.Id

data class Recipe(
    val id: Id<Recipe>,
    val title: String,
)
