package de.frinshhd.streamlineai.routes

import de.frinshhd.streamlineai.api.AuthResponse
import de.frinshhd.streamlineai.auth.models.GoogleUser
import de.frinshhd.streamlineai.data.UserRepository
import de.frinshhd.streamlineai.models.User
import de.frinshhd.streamlineai.util.GoogleTokenVerifierUtil
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.userRoutes() {
    route("/users") {
        post("/google") {
            val user = call.receive<GoogleUser>()
            val payload = GoogleTokenVerifierUtil.verify(user.idToken)
            if (payload == null) {
                call.respond(mapOf("status" to "error", "message" to "Invalid Google token"))
                return@post
            }
            val googleId = payload.subject
            val email = payload["email"] as? String ?: ""
            val name = payload["name"] as? String ?: ""
            val picture = payload["picture"] as? String
            val existingUser = UserRepository.getUser(googleId)
            val finalUser = if (existingUser != null) {
                existingUser
            } else {
                val newUser = User(
                    id = googleId,
                    idToken = user.idToken,
                    name = name,
                    email = email,
                    pictureUrl = picture
                )
                newUser
            }
            UserRepository.saveUser(finalUser)
            val token = UUID.randomUUID().toString()
            UserRepository.saveToken(token, finalUser.id)
            call.respond(AuthResponse(token, finalUser))
        }
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)
            val authHeader = call.request.headers["Authorization"]
            val token = authHeader?.removePrefix("Bearer ")
            val userId = token?.let { UserRepository.getUserIdByToken(it) }
            if (userId == null || userId != id) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("status" to "error", "message" to "Invalid or missing token"))
                return@get
            }
            val user = UserRepository.getUser(id)
            if (user != null) {
                call.respond(user)
            } else {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            }
        }

        get("/protected") {
            println("Accessing protected route")

            val authHeader = call.request.headers["Authorization"]
            val token = authHeader?.removePrefix("Bearer ")
            val userId = token?.let { UserRepository.getUserIdByToken(it) }
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("status" to "error", "message" to "Invalid or missing token"))
                return@get
            }
            val user = UserRepository.getUser(userId)
            if (user != null) {
                call.respond(ProtectedRouteResponse("ok", "Yes", user))
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("status" to "error", "message" to "User not found"))
            }
        }

        get {
            call.respond(UserRepository.getAllUsers())
        }
    }
}

@kotlinx.serialization.Serializable
data class ProtectedRouteResponse(
    val status: String,
    val message: String,
    val user: User? = null
)
