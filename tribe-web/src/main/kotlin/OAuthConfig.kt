package no.sonhal

import io.github.cdimascio.dotenv.dotenv
import io.ktor.auth.*
import io.ktor.http.*

private val dotenv = dotenv()

val loginProvider
    get() =  OAuthServerSettings.OAuth2ServerSettings(
        name = dotenv["name"],
        authorizeUrl = dotenv["authorizeUrl"],
        accessTokenUrl = dotenv["accessTokenUrl"],
        requestMethod = HttpMethod.Post,
        clientId = dotenv["clientId"],
        clientSecret = dotenv["clientSecret"],
        defaultScopes = listOf("todoAPI.read", "todoAPI.write"),
        authorizeUrlInterceptor = { this.parameters.append("audience", "http://0.0.0.0:8090/api/todos")}
)