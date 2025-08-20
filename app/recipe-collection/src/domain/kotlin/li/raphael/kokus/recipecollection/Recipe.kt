package li.raphael.kokus.recipecollection

import li.raphael.kokus.fundamentals.RecipeId

data class Recipe(
    val id: RecipeId,
    val title: String,
)
