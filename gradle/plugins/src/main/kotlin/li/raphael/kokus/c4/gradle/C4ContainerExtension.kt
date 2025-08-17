package li.raphael.kokus.c4.gradle

import li.raphael.kokus.c4.core.C4ContainerRelation
import li.raphael.kokus.c4.core.C4Element
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

abstract class C4ContainerExtension {
    abstract val name: Property<String>
    abstract val description: Property<String>
    abstract val technology: Property<String>
    abstract val tags: ListProperty<String>
    internal abstract val incomingRelations: ListProperty<C4ContainerRelation>
    internal abstract val outgoingRelations: ListProperty<C4ContainerRelation>
    abstract val hidden: Property<Boolean>

    fun relations(block: RelationContainer.() -> Unit) {
        RelationContainer()
            .apply(block)
            .let {
                incomingRelations.set(it.incomingRelations)
                outgoingRelations.set(it.outgoingRelations)
            }
    }

    class RelationContainer {
        internal val incomingRelations = mutableListOf<C4ContainerRelation>()
        internal val outgoingRelations = mutableListOf<C4ContainerRelation>()

        fun incoming(
            from: C4Element,
            description: String,
            protocol: String,
        ) {
            incomingRelations.add(C4ContainerRelation(from, protocol, description))
        }

        fun outgoing(
            to: C4Element,
            description: String,
            protocol: String,
        ) {
            outgoingRelations.add(C4ContainerRelation(to, protocol, description))
        }
    }
}
