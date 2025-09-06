package li.raphael.kokus.recipecollection

class RecipeCollection internal constructor(
    private val recipeRepository: RecipeRepository,
) {
    fun findAll(): List<Recipe> = recipeRepository.findAll()
}
