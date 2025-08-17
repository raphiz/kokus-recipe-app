// No serialVersionUID needed: config cache invalidates on build-logic changes
@file:Suppress("SerialVersionUIDInSerializableClass")

package li.raphael.kokus.c4.core

import java.io.Serializable
import java.util.*

enum class DiagramType(
    val title: String,
) {
    CONTAINER("Container"),
    SYSTEM_CONTEXT("System Context"),
}

@JvmInline
value class ProjectId(
    val path: String,
)

// Sorted sets used for reproducible build caches
data class C4Project(
    val projectId: ProjectId,
    val name: String,
    val description: String,
    val technology: String,
    val tags: SortedSet<String>,
    val dependencies: SortedSet<C4ContainerDependency>,
    val incomingRelations: SortedSet<C4ContainerRelation>,
    val outgoingRelations: SortedSet<C4ContainerRelation>,
    val hidden: Boolean,
) : Serializable

data class C4ContainerDependency(
    val projectId: ProjectId,
    val scope: String,
    val description: String? = null,
    val technology: String? = null,
) : Serializable,
    Comparable<C4ContainerDependency> {
    override fun compareTo(other: C4ContainerDependency) = compareValuesBy(this, other, { it.projectId.path }, { it.scope })
}

data class C4ContainerRelation(
    val target: C4Element,
    val technology: String,
    val description: String,
) : Serializable,
    Comparable<C4ContainerRelation> {
    override fun compareTo(other: C4ContainerRelation) = compareValuesBy(this, other, { it.target.name }, { it.description })
}

sealed interface C4Element {
    val name: String
    val description: String

    data class SoftwareSystem(
        override val name: String,
        override val description: String,
    ) : C4Element,
        Serializable

    data class Person(
        override val name: String,
        override val description: String,
    ) : C4Element,
        Serializable
}
