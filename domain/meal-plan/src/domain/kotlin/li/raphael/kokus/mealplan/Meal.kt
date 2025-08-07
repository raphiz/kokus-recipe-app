package li.raphael.kokus.mealplan

import li.raphael.kokus.base.Id
import li.raphael.kokus.recipecollection.Recipe
import java.time.LocalDate

data class Meal(
    val id: Id<Meal>,
    val recipeId: Id<Recipe>,
    val date: LocalDate?,
)
