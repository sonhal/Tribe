
group = "no.sonhal"
version = "0.0.1"

plugins {
    application
}

application {
    mainClassName = "io.ktor.server.cio.EngineMain"
}

dependencies {
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.postgresql:postgresql:42.2.18")
    implementation("org.flywaydb:flyway-core:6.5.0")
    implementation("com.auth0:java-jwt:3.12.1")
    implementation("no.nav.security:token-validation-ktor:1.3.3")
}