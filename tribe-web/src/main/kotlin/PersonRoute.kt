package no.sonhal

import io.ktor.application.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Routing.people() {

    get("/people") {
        call.respond(MustacheContent("people.hbs", mapOf("people" to personDao.everyone().serialized())))
    }
}