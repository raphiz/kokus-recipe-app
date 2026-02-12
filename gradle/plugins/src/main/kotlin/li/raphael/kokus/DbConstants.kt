package li.raphael.kokus

import org.gradle.api.Project
import org.gradle.api.provider.Provider

// These values must be in sync with configured values in the docker-compose file
val Project.SKIP_DB get() = buildParameter("db.skip").orElse("false")
val Project.DB_PORT get() = buildParameter("db.port").orElse("5432")
val Project.DB_USERNAME get() = buildParameter("db.user").orElse("localdev")
val Project.DB_PASSWORD get() = buildParameter("db.password").orElse("*****")
val Project.DB_JDBC_URL get() =
    buildParameter("db.jdbc.url")
        .orElse(DB_PORT.map { "jdbc:postgresql://localhost:$it/" })

private fun Project.buildParameter(gradleKey: String): Provider<String> {
    val environmentVariableKey = gradleKey.uppercase().replace(".", "_")
    return providers
        .gradleProperty(gradleKey)
        .orElse(providers.environmentVariable(environmentVariableKey))
}
