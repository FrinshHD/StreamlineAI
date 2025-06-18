package de.frinshhd.streamlineai.api

import de.frinshhd.streamlineai.models.User
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

object ApiClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = "wiwaxia.fritz.box"
                port = 8080
            }
        }
    }
}

@Serializable
data class AuthResponse(
    val token: String,
    val user: User
)
