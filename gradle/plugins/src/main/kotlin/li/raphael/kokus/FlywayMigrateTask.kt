package li.raphael.kokus

import org.flywaydb.core.Flyway
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class FlywayMigrateTask : DefaultTask() {
    @get:Input
    abstract val url: Property<String>

    @get:Input
    abstract val username: Property<String>

    @get:Input
    abstract val password: Property<String>

    @get:InputDirectory
    abstract var migrationsLocation: Directory

    @TaskAction
    fun run() {
        val flyway =
            Flyway
                .configure()
                .dataSource(url.get(), username.get(), password.get())
                .cleanDisabled(false)
                .locations("filesystem:${migrationsLocation.asFile}")
                .load()

        if (!flyway.validateWithResult().validationSuccessful) {
            flyway.clean()
        }

        flyway
            .migrate()
    }
}
