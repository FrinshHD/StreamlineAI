package de.frinshhd.streamlineai.auth

import de.frinshhd.streamlineai.Env
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureGoogleOAuth() {
    install(Authentication) {
        oauth("auth-oauth-google") {
            urlProvider = { "http://wiwaxia.fritz.box:8080/callback" }
            providerLookup = {
                try {
                    OAuthServerSettings.OAuth2ServerSettings(
                        name = "google",
                        authorizeUrl = "https://accounts.google.com/o/oauth2/v2/auth",
                        accessTokenUrl = "https://oauth2.googleapis.com/token",
                        clientId = Env.dotenv["GOOGLE_CLIENT_ID"] ?: throw IllegalStateException("Missing GOOGLE_CLIENT_ID env var"),
                        clientSecret = Env.dotenv["GOOGLE_CLIENT_SECRET"] ?: throw IllegalStateException("Missing GOOGLE_CLIENT_SECRET env var"),
                        requestMethod = HttpMethod.Post,
                        defaultScopes = listOf("profile", "email")
                    )
                } catch (e: Exception) {
                    // Log the error and rethrow to fail fast
                    this@configureGoogleOAuth.log.error("Google OAuth configuration error: ${e.message}", e)
                    throw e
                }
            }
            client = try {
                HttpClient(Apache)
            } catch (e: Exception) {
                this@configureGoogleOAuth.log.error("Failed to create HttpClient for Google OAuth: ${e.message}", e)
                throw e
            }
        }
    }
}