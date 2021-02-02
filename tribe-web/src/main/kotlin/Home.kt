package no.sonhal

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*


fun Routing.home() {

    get("/") {
        call.respond(MustacheContent("index.hbs", mapOf("user" to MustacheUser(1, "user1"))))
    }

    static("/css") {
        resources("static/css")
    }

}

fun Routing.login() {

    get("/login") {
        call.respond(MustacheContent("index.hbs", mapOf("user" to MustacheUser(1, "user1"))))
    }

}