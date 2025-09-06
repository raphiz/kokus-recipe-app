package li.raphael.kokus.recipecollection

interface RecipeRepository {
    fun findAll(): List<Recipe>
}
