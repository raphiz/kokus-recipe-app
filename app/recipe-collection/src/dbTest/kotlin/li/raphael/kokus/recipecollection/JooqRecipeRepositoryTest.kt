package li.raphael.kokus.recipecollection

import li.raphael.kokus.fundamentals.RecipeId
import org.junit.jupiter.api.Test

class JooqRecipeRepositoryTest {
    @Test
    fun mvp() =
        withDatabase { dsl ->
            val repo = JooqRecipeRepository(dsl)
            repo.save(Recipe(id = RecipeId.random(), title = "Test"))
            println(repo.findAll())
        }
}
