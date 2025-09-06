import gradle.kotlin.dsl.accessors._3a58d6fb01bb07dfb0dff4a5039430f9.sourceSets
import li.raphael.kokus.FlywayMigrateTask
import li.raphael.kokus.invoke
import li.raphael.kokus.libs
import li.raphael.kokus.registeringFacet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.codegen.gradle.CodegenPluginExtension
import org.jooq.codegen.gradle.CodegenTask

plugins {
    id("org.jooq.jooq-codegen-gradle")
}

// These values must be in sync with configured values in the docker-compose file
val dbPort = "5432"
val dbUsername = "localdev"
val dbPassword = "*****"

val dbJdbcUrl = "jdbc:postgresql://localhost:$dbPort/"
val dbTemplateDbName = "template1"
val dbMigrationsLocation = layout.projectDirectory.dir("src/main/resources/db/migration")
val dbTemplateDbUrl = "${dbJdbcUrl}$dbTemplateDbName"

// Use a minimal custom FlywayMigrate task
// The official flyway Gradle plugin does too much and is not well maintained
val flywayMigrate by tasks.registering(FlywayMigrateTask::class) {
    url.convention(dbTemplateDbUrl)
    username.convention(dbUsername)
    password.convention(dbPassword)
    migrationsLocation = dbMigrationsLocation
}

val jooq by registeringFacet()
val dbJooqOutputDirectory = layout.projectDirectory.dir("src/jooq/kotlin")

dependencies {
    jooq.apiConfigurationName(libs("jooq"))
}

extensions.configure<CodegenPluginExtension> {
    configuration {
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            jdbc {
                driver = "org.postgresql.Driver"
                url = dbTemplateDbUrl
                user = dbUsername
                password = dbPassword
            }
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                includes = "public.*"
                excludes = "flyway_schema_history"
                inputSchema = "public"
                isOutputSchemaToDefault = true
            }
            target {
                directory = dbJooqOutputDirectory.asFile.absolutePath
                packageName = "li.raphael.kokus.db.jooq"
            }
        }
    }
}

tasks.named<CodegenTask>("jooqCodegen").configure {
    // DB must be migrated before jOOQ can generate the code.
    // This task can be cached because [[flyway migrations] + [postgres (pinned in docker compose)] + [jooq]] -> generated code.
    dependsOn(flywayMigrate)
    inputs.files(
        dbMigrationsLocation,
        rootProject.file("docker-compose.yaml"),
    )

    // By default, the jOOQ Gradle plugin adds the generated sources to the main source set.
    // so we make sure it's removed:
    sourceSets.named("main").configure {
        java.setSrcDirs(java.srcDirs - dbJooqOutputDirectory.asFile)
    }
}

tasks.named<KotlinCompile>("compileJooqKotlin").configure {
    dependsOn(tasks.named("jooqCodegen"))
}
