package no.sonhal

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.mustache.Mustache
import io.ktor.sessions.*
import io.ktor.auth.*
import io.github.cdimascio.dotenv.dotenv
import io.ktor.features.*
import io.ktor.client.*
import org.slf4j.event.Level


val oauthauthentication = "oauthauthentication"
val oauthIp = dotenv()["oauthIP"]

val datasource = DataSourceBuilder().dataSource
val personDao = PersonDao(datasource)

fun main(args: Array<String>) {
    datasource.migrate()
    io.ktor.server.cio.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(CallLogging) {
        level = Level.INFO
    }

    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates/mustache")
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

    install(Routing) {
        trace {
            application.log.trace(it.buildText()) }
        home()
        login()
        people()
    }

}

data class MustacheUser(val id: Int, val name: String)

data class UserSession(val id: String, val name: String, val token: String )

fun ApplicationCall.redirectUrl(path: String): String {
    val defaultPort = if (request.origin.scheme == "http") 80 else 443
    val hostPort = request.host() + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    return "$protocol://$hostPort$path"
}