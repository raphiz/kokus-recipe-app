package li.raphael.kokus.mealplan

import li.raphael.kokus.base.MealId
import li.raphael.kokus.base.RecipeId
import java.time.LocalDate

data class Meal(
    val id: MealId,
    val recipeId: RecipeId,
    val date: LocalDate?,
)
