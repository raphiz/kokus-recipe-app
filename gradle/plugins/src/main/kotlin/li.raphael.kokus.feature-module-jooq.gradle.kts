import li.raphael.kokus.*
import org.jetbrains.kotlin.gradle.utils.extendsFrom

plugins {
    id("li.raphael.kokus.module")
    id("jvm-test-suite")
}

val db by registeringFacet()

testing {
    suites {
        register<JvmTestSuite>("dbTest") {
            useJUnitJupiter()
        }
    }
}
val dbTestTask = tasks.named<Test>("dbTest")

tasks.named("check").configure {
    dependsOn(dbTestTask)
}

dbTestTask.configure {
    enabled = (SKIP_DB.get().lowercase() != "true")
    dependsOn(":app:fundamentals:database:flywayMigrate")

    environment("DB_URL", DB_JDBC_URL.get())
    environment("DB_USER", DB_USERNAME.get())
    environment("DB_PASSWORD", DB_PASSWORD.get())
    environment("DB_TEMPLATE_NAME", "template1")
    systemProperty("org.jooq.no-logo", "true")
    systemProperty("org.jooq.no-tips", "true")
}

val dbTestImplementation = configurations.named("dbTestImplementation")
val dbTestRuntimeOnly = configurations.named("dbTestRuntimeOnly")
val dbImplementation = configurations.named(db.implementationConfigurationName)
val dbRuntimeOnly = configurations.named(db.runtimeOnlyConfigurationName)

dependencies {
    dbImplementation(facet(project(path), "domain"))
    dbImplementation(facet(project(":app:fundamentals:database"), "jooq"))
    dbRuntimeOnly(libs("postgresql"))

    dbTestImplementation(facet(project(path), "db"))

    dbTestImplementation.extendsFrom(dbImplementation)
    dbTestRuntimeOnly.extendsFrom(dbRuntimeOnly)
}
