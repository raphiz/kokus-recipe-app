package li.raphael.kokus.fundamentals

import java.lang.reflect.Constructor
import java.util.UUID
import kotlin.reflect.KClass

data class RecipeId(
    override val value: UUID,
) : Id {
    companion object : IdFactory<RecipeId>(RecipeId::class)
}

data class MealId(
    override val value: UUID,
) : Id {
    companion object : IdFactory<MealId>(MealId::class)
}

interface Id {
    val value: UUID
}

abstract class IdFactory<ID : Id>(
    private val idConstructor: Constructor<ID>,
) {
    constructor(idKlass: KClass<ID>) : this(idKlass.java.getConstructor(UUID::class.java))

    private fun createId(value: UUID): ID = idConstructor.newInstance(value)

    fun random(): ID = createId(UUID.randomUUID())
}
