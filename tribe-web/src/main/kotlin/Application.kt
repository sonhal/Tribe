package no.sonhal

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.mustache.Mustache
import io.ktor.mustache.MustacheContent
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.sessions.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.client.*


val oauthauthentication = "oauthauthentication"

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {


    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates/mustache")
    }

    routing {
        home()
    }

    install(Sessions) {
        cookie<UserSession>("t_session", storage = SessionStorageMemory())
    }

    install(Authentication) {
        oauth(oauthauthentication) {
            skipWhen { call -> call.sessions.get<UserSession>() != null }

            providerLookup = {
                loginProvider
            }
            client = HttpClient().apply {
                environment.monitor.subscribe(ApplicationStopping) {
                    close()
                }
            }
            urlProvider = {redirectUrl("/login")}
        }
    }
}

data class MustacheUser(val id: Int, val name: String)

data class UserSession(val id: String, val name: String, val token: String )

private fun ApplicationCall.redirectUrl(path: String): String {
    val defaultPort = if (request.origin.scheme == "http") 80 else 443
    val hostPort = request.host() + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    return "$protocol://$hostPort$path"
}