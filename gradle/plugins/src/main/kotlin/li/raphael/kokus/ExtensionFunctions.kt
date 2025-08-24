package li.raphael.kokus

import org.gradle.api.Project
import org.gradle.api.artifacts.*
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getByType
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// This does the same as the `testFixtures` function
// See https://github.com/gradle/gradle/blob/674b8430b024f03cae24f1e4dd6dbaa78b557dae/platforms/software/dependency-management/src/main/java/org/gradle/api/internal/artifacts/dsl/dependencies/DefaultDependencyHandler.java#L369
fun DependencyHandler.facet(
    projectPath: ProjectDependency,
    facetName: String,
): Dependency {
    val facetDependency = create(projectPath)
    require(facetDependency is ModuleDependency)

    facetDependency.capabilities {
        requireFeature(facetName)
    }

    return facetDependency
}

fun Project.registeringFacet(): ReadOnlyProperty<Any?, SourceSet> =
    object : ReadOnlyProperty<Any?, SourceSet> {
        private var cached: SourceSet? = null

        override fun getValue(
            thisRef: Any?,
            property: KProperty<*>,
        ): SourceSet =
            cached ?: project.registerFacet(property.name).also {
                cached = it
            }
    }

fun Project.registerFacet(facetName: String): SourceSet {
    val sourceSets = extensions.getByType<SourceSetContainer>()
    val java = extensions.getByType<JavaPluginExtension>()

    val facetSourceSet = sourceSets.maybeCreate(facetName)
    java.registerFeature(facetName) {
        usingSourceSet(facetSourceSet)
    }

    return facetSourceSet
}

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

operator fun VersionCatalog.invoke(alias: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(alias).orElseThrow {
        error("version catalog 'libs' has no library alias '$alias'")
    }
