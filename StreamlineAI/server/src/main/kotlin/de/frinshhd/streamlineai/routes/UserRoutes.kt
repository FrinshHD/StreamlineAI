package de.frinshhd.streamlineai.routes

import de.frinshhd.streamlineai.session.UserSession
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Routing.userRoutes() {
    get("/profile") {
        val session = call.sessions.get<UserSession>()
        if (session != null) {
            call.respondText("Welcome ${session.email}")
        } else {
            call.respondRedirect("/login")
        }
    }
}