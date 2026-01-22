package li.raphael.kokus.fundamentals

import java.util.UUID

data class RecipeId(
    override val value: UUID,
) : Id {
    companion object : IdFactory<RecipeId>(::RecipeId)
}

data class MealId(
    override val value: UUID,
) : Id {
    companion object : IdFactory<MealId>(::MealId)
}

interface Id {
    val value: UUID
}

abstract class IdFactory<T : Id>(
    private val factory: (UUID) -> T,
) {
    fun random(): T = factory(UUID.randomUUID())

    fun from(uuid: UUID): T = factory(uuid)

    fun from(string: String): T = factory(UUID.fromString(string))
}
