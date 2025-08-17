import li.raphael.kokus.c4.gradle.C4ContainerExtension
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

extensions.create("c4Container", C4ContainerExtension::class.java).apply {
    name.convention(project.name.uppercaseFirstChar())
    technology.convention(
        provider {
            when {
                plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> "Kotlin Module"
                plugins.hasPlugin("groovy") -> "Groovy Module"
                plugins.hasPlugin("java") -> "Kotlin Module"
                else -> null
            }
        },
    )
    tags.convention(mutableListOf())
    hidden.convention(false)
}
