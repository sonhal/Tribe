package no.sonhal

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import io.ktor.util.pipeline.*


fun Routing.home() {

    get("/") {
        call.respond(MustacheContent("index.hbs", mapOf("user" to MustacheUser(1, "user1"))))
    }

    static("/css") {
        resources("static/css")
    }

}

fun Routing.login() {
    authenticate(oauthauthentication) {
        get("/login") {
            when(val userSession = call.sessions.get<UserSession>()) {
                null -> {
                    getJWT()?.also {
                        call.sessions.set(UserSession(id = it.subject, name = "User", token = it.token))
                        call.respond(MustacheContent("login.hbs", mapOf("session" to call.sessions.get<UserSession>())))
                    } ?: call.respond(MustacheContent("login-failed.hbs", mapOf("error" to call.parameters.getAll("error").orEmpty().toString())))
                }
                else -> {
                    call.respond(MustacheContent("login.hbs", mapOf("session" to userSession)))
                }
            }
        }

        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect {
                port = DEFAULT_PORT
                protocol = URLProtocol.HTTPS
                host = oauthIp
                encodedPath = "/v2/logout"
                parameters.append("client_id", loginProvider.clientId)
                parameters.append("returnTo", call.redirectUrl("/"))
            }
        }
    }

}
private fun PipelineContext<Unit, ApplicationCall>.getJWT(): DecodedJWT? {
    val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
    val jwt = JWT.decode(principal?.accessToken)
    return jwt
}