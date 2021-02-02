
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
    implementation("org.flywaydb:flyway-core:6.5.0")
}