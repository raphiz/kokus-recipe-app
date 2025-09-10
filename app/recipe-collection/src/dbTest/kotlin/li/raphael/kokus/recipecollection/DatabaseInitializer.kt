package li.raphael.kokus.recipecollection

import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.sql.Connection
import java.sql.DriverManager
import java.time.Duration

val databaseInitializer =
    DatabaseInitializer(
        baseUrl = System.getenv("DB_URL"),
        username = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD"),
        templateDbName = System.getenv("DB_TEMPLATE_NAME"),
        testDbName = "testdb_${System.getProperty("org.gradle.test.worker")}",
    ).also {
        it.initialize()
        Runtime.getRuntime().addShutdownHook(Thread { it.close() })
    }

fun withDatabase(block: (dsl: DSLContext) -> Unit) =
    DSL.using(databaseInitializer.url, databaseInitializer.username, databaseInitializer.password).use { dsl ->
        dsl.transaction { configuration ->
            try {
                block(configuration.dsl())
            } finally {
                dsl.rollback().execute()
            }
        }
    }

class DatabaseInitializer(
    baseUrl: String,
    val username: String,
    val password: String,
    private val templateDbName: String,
    private val testDbName: String,
) : AutoCloseable {
    val url = "$baseUrl$testDbName"
    private val connection by lazy { DriverManager.getConnection(baseUrl, username, password) }

    fun initialize() {
        connection.execute(
            """
            DROP DATABASE IF EXISTS $testDbName WITH (FORCE);
            CREATE DATABASE $testDbName TEMPLATE $templateDbName;
            """.trimIndent(),
        )
    }

    override fun close() {
        connection.use { connection ->
            connection.execute("DROP DATABASE IF EXISTS $testDbName WITH (FORCE);")
        }
    }

    private fun Connection.execute(
        query: String,
        @Suppress("MagicNumber")
        timeout: Duration = Duration.ofSeconds(5),
    ) {
        this.createStatement().use {
            it.queryTimeout = timeout.toSeconds().toInt()
            it.execute(query)
        }
    }
}
