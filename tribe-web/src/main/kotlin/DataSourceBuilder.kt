package no.sonhal

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.flywaydb.core.Flyway

class DataSourceBuilder(
    dbName: String? = null,
    dbHost: String? = null,
    dbPort: String? = null,
    dbUsername: String? = null,
    dbPassword: String? = null
) {

    val dataSource = HikariDataSource(hikariConfig(
        dbName ?: dotenv()["DB_NAME"],
        dbHost ?: dotenv()["DB_HOST"],
        dbPort ?: dotenv()["DB_PORT"],
        dbUsername ?:dotenv()["DB_USERNAME"],
        dbPassword ?: dotenv()["DB_PASSWORD"],))

    private fun hikariConfig(
        dbName: String,
        dbHost: String,
        dbPort: String,
        dbUsername: String,
        dbPassword: String
    ) = HikariConfig().apply {
        jdbcUrl = dotenv()["DATABASE_JDBC_URL"] ?: String.format(
            "jdbc:postgresql://%s:%s/%s", dbHost, dbPort,
            dbName
        )
        username = dbUsername
        password = dbPassword

        maximumPoolSize = 9
        minimumIdle = 1
        idleTimeout = 10001
        connectionTimeout = 1000
        maxLifetime = 30001
        leakDetectionThreshold = 30000
    }


}

fun HikariDataSource.migrate() {
    Flyway.configure()
        .dataSource(this)
        .load()
        .migrate()
}