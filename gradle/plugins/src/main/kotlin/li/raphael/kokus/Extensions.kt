package li.raphael.kokus

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler

// This does the same as the `testFixtures` function
// See https://github.com/gradle/gradle/blob/674b8430b024f03cae24f1e4dd6dbaa78b557dae/platforms/software/dependency-management/src/main/java/org/gradle/api/internal/artifacts/dsl/dependencies/DefaultDependencyHandler.java#L369
fun DependencyHandler.feature(
    projectPath: ProjectDependency,
    featureName: String,
): Dependency {
    val featureDependency = create(projectPath)
    require(featureDependency is ModuleDependency)

    featureDependency.capabilities {
        requireFeature(featureName)
    }

    return featureDependency
}
