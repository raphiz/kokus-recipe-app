package li.raphael.kokus.c4.gradle

import li.raphael.kokus.c4.core.C4ContainerRelation
import li.raphael.kokus.c4.gradle.C4ContainerExtension.RelationContainer
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

abstract class C4DiagramsExtension {
    abstract val title: Property<String>
    abstract val description: Property<String>
    abstract val technology: Property<String>
    abstract val tags: ListProperty<String>
    internal abstract val incomingRelations: ListProperty<C4ContainerRelation>
    internal abstract val outgoingRelations: ListProperty<C4ContainerRelation>

    fun relations(block: RelationContainer.() -> Unit) {
        RelationContainer()
            .apply(block)
            .let {
                incomingRelations.set(it.incomingRelations)
                outgoingRelations.set(it.outgoingRelations)
            }
    }
}
