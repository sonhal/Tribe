package no.sonhal


import io.ktor.http.*
import com.opentable.db.postgres.embedded.EmbeddedPostgres
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.testing.*
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Test
import java.nio.file.Files.createTempDirectory
import kotlin.test.assertEquals

class ApplicationTest {

    companion object {

        private val hikariConfig = HikariConfig().apply {
            jdbcUrl = this@Companion.getJdbcUrl()
            maximumPoolSize = 5
            minimumIdle = 1
            idleTimeout = 500001
            connectionTimeout = 1000
            maxLifetime = 600001
        }

        fun startEmbeddedPostgres(): EmbeddedPostgres {
            val postgresPath = createTempDirectory("testdb").toFile()
            return EmbeddedPostgres.builder()
                .setOverrideWorkingDirectory(postgresPath)
                .setDataDirectory(postgresPath.resolve("datadir"))
                .start()
        }

        private fun getJdbcUrl() = startEmbeddedPostgres().getJdbcUrl("postgres", "postgres")
        internal val dataSource = HikariDataSource(hikariConfig)

        init {
            Flyway.configure()
                .dataSource(dataSource)
                .load()
                .migrate()
        }
    }


    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }

    @Test
    fun testDb() {
        testQuery()
        println("Hello")
    }

    fun testQuery() {
        using(sessionOf(dataSource)) {
            it.run(
                queryOf(
                    "INSERT INTO PERSON(id, name) VALUES(?, ?)",
                    1,
                    "Sondre",
                ).asExecute
            )
        }
    }
}
