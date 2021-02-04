package no.sonhal

import java.util.*

class Person(private val id: String, private val name: String, private val email: String) {

    fun persist(doa: PersonDao) {
        doa.persistPerson(id, name, email)
    }

    fun serialize(): Map<String, String> {
        return mapOf(
            "id" to id,
            "name" to name,
            "email" to email
        )
    }
}