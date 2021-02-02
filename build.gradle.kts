import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val junitJupiterVersion = "5.6.2"

plugins {
    java
    kotlin("jvm") version "1.4.21"
}

group = "no.sonhal"
version = "0.0.1"



repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
    }
}

allprojects {
    group = "no.sonhal"
    version = "0.0.1"

    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        jcenter()
        mavenLocal()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
        implementation("io.ktor:ktor-server-cio:$ktor_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")
        implementation("io.ktor:ktor-server-core:$ktor_version")
        implementation("io.ktor:ktor-mustache:$ktor_version")
        implementation("io.ktor:ktor-server-host-common:$ktor_version")
        implementation("io.ktor:ktor-locations:$ktor_version")
        implementation("io.ktor:ktor-metrics:$ktor_version")
        implementation("io.ktor:ktor-server-sessions:$ktor_version")
        implementation("io.ktor:ktor-auth:$ktor_version")
        implementation("io.ktor:ktor-jackson:$ktor_version")
        implementation("io.ktor:ktor-client-core:$ktor_version")
        implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
        implementation("io.ktor:ktor-client-cio:$ktor_version")
        implementation("com.github.seratch:kotliquery:1.3.1")
        implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")

        testImplementation("io.ktor:ktor-server-tests:$ktor_version")
        testImplementation("com.opentable.components:otj-pg-embedded:0.13.3")
        testImplementation("io.mockk:mockk:1.10.0")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    }

    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = "14"
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "14"
        }

        withType<Test> {
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }
}