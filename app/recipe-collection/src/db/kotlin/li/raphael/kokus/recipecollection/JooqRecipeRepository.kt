package li.raphael.kokus.recipecollection

import li.raphael.kokus.db.jooq.tables.records.RecipeRecord
import li.raphael.kokus.db.jooq.tables.references.RECIPE
import li.raphael.kokus.fundamentals.RecipeId
import org.jooq.DSLContext

class JooqRecipeRepository(
    private val dsl: DSLContext,
) : RecipeRepository {
    override fun findAll(): List<Recipe> = dsl.selectFrom(RECIPE).fetch { it.toRecipe() }

    fun save(recipe: Recipe) = dsl.insertInto(RECIPE).set(recipe.toRecord()).execute()
}

private fun RecipeRecord.toRecipe() = Recipe(RecipeId(id!!), title!!)

private fun Recipe.toRecord() = RecipeRecord(id.value, title)
