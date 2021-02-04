package no.sonhal

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import kotliquery.queryOf
import kotliquery.sessionOf
import org.intellij.lang.annotations.Language


class PersonDao(private val dataSource: HikariDataSource) {

    constructor(dbName: String, dbHost: String,dbPort: String) :
            this(DataSourceBuilder(dbName, dbHost, dbPort).dataSource)


    fun persistPerson(id: String, name: String, email: String): Int =
        sessionOf(dataSource, returnGeneratedKey = false).use { session ->
            @Language("PostgreSQL")
            val query = """
            INSERT INTO PERSON(id, name, email)
            VALUES(:id, :name, :email);
        """
            requireNotNull(
                session.run(
                    queryOf(
                        query,
                        mapOf(
                            "id" to id,
                            "name" to name,
                            "email" to email,
                        )
                    ).asUpdate
                )
            )
        }

    fun everyone() = sessionOf(dataSource, returnGeneratedKey = false).use { session ->
        @Language("PostgreSQL")
        val query = """
            SELECT id, name, email FROM PERSON;
        """
        requireNotNull(
            session.run(
                queryOf(
                    query,
                ).map { row ->
                    Person(
                        row.string("id"),
                        row.string("name"),
                        row.string("email")
                    )
                }.asList
            )
        )
    }

}

fun List<Person>.serialized(): List<Map<String, String>> {
    return this.map { it.serialize() }
}