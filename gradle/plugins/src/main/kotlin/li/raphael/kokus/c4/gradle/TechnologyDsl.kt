package li.raphael.kokus.c4.gradle

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import java.util.concurrent.ConcurrentHashMap

private val dependencyTechnologies = ConcurrentHashMap<String, String>()

fun Dependency.withTechnology(technology: String): Dependency {
    if (this is ProjectDependency) {
        val key = "${this.group}:${this.name}:${this.version}:${this.targetConfiguration}"
        dependencyTechnologies[key] = technology
    }
    return this
}

internal fun Dependency.getTechnology(): String? {
    if (this is ProjectDependency) {
        val key = "${this.group}:${this.name}:${this.version}:${this.targetConfiguration}"
        return dependencyTechnologies[key]
    }
    return null
}
