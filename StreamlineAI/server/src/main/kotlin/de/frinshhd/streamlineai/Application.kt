package de.frinshhd.streamlineai

import de.frinshhd.streamlineai.auth.configureGoogleOAuth
import de.frinshhd.streamlineai.auth.oauthRoutes
import de.frinshhd.streamlineai.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

object Env {
    val dotenv by lazy { io.github.cdimascio.dotenv.dotenv() }
}

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureGoogleOAuth()
    routing {
        oauthRoutes()
        userRoutes()

        get("/") {
            call.respondText("Welcome to Streamline AI Backend!")
        }
    }
}