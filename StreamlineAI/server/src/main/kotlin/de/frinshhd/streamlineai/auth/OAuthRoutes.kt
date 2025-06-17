package de.frinshhd.streamlineai.auth

import de.frinshhd.streamlineai.session.UserSession
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Routing.oauthRoutes() {
    authenticate("auth-oauth-google") {
        get("/login") {
            try {
                // This will redirect to Google, handled by Ktor's oauth feature
            } catch (e: Exception) {
                call.respond(
                    mapOf(
                        "success" to false,
                        "error" to (e.message ?: "Unknown error during login redirect")
                    )
                )
            }
        }

        get("/callback") {
            try {
                val principal = call.principal<OAuthAccessTokenResponse.OAuth2>()
                if (principal == null) {
                    call.respond(
                        mapOf(
                            "success" to false,
                            "error" to "No OAuth principal found."
                        )
                    )
                    return@get
                }
                // TODO: Fetch the user's email from Google using the access token
                val email = "user@example.com" // Replace with actual email fetching logic
                call.sessions.set(UserSession(email))
                call.respondText("""
                    <html>
                    <body>
                        <script>
                            window.onload = function() {
                                window.close();
                            }
                        </script>
                        <h2>Login successful!</h2>
                        <p>Your email: $email</p>
                        <p>You can close this window.</p>
                    </body>
                    </html>
                """, ContentType.Text.Html)
            } catch (e: Exception) {
                call.respond(
                    mapOf(
                        "success" to false,
                        "error" to (e.message ?: "Unknown error during OAuth callback")
                    )
                )
            }
        }
    }
}