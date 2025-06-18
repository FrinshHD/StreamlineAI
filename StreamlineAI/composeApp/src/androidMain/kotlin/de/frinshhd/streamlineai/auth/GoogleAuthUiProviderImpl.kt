package de.frinshhd.streamlineai.auth

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import de.frinshhd.streamlineai.api.ApiClient.client
import de.frinshhd.streamlineai.api.AuthResponse
import de.frinshhd.streamlineai.auth.models.GoogleAuthCredentials
import de.frinshhd.streamlineai.auth.models.GoogleUser
import de.frinshhd.streamlineai.models.User
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class GoogleAuthUiProviderImpl(
    private val activityContext: Context,
    private val credentialManager: CredentialManager,
    private val credentials: GoogleAuthCredentials,
) :
    GoogleAuthUiProvider {
    override suspend fun signIn(): User? {
        var user: User? = null

        try {
            val credential = credentialManager.getCredential(
                context = activityContext,
                request = getCredentialRequest()
            ).credential
            val googleUser = getGoogleUserFromCredential(credential)
            googleUser?.let {
                user = sendUserToBackend(it)
            }
        } catch (e: GetCredentialException) {
            Log.e("StreamlineAI", "GoogleAuthUiProvider error: ${e.message}")
            null
        } catch (_: NullPointerException) {
            null
        }

        return user
    }

    private suspend fun sendUserToBackend(user: GoogleUser): User? = withContext(Dispatchers.IO) {
        try {
            val response = client.post {
                url("/users/google")
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            val authResponse = response.body<AuthResponse>()
            Log.d("StreamlineAI", "User sent to backend: $authResponse")

            val response2 = client.get {
                url("/users/protected")
                header(HttpHeaders.Authorization, "Bearer ${authResponse.token}")
                parameter("id", authResponse.user.id)
            }

            Log.d("StreamlineAI", "Protected resource response: $response2")

            return@withContext authResponse.user
        } catch (e: Exception) {
            Log.e("StreamlineAI", "Failed to send user to backend: ${e.message}")
            null
        }
    }

    private fun getGoogleUserFromCredential(credential: Credential): GoogleUser? {
        return when {
            credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    GoogleUser(
                        idToken = googleIdTokenCredential.idToken,
                        displayName = googleIdTokenCredential.displayName ?: "",
                        profilePicUrl = googleIdTokenCredential.profilePictureUri?.toString()
                    )
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e(
                        "StreamlineAI",
                        "GoogleAuthUiProvider Received an invalid google id token response: ${e.message}"
                    )
                    null
                }
            }

            else -> null
        }
    }

    private fun getCredentialRequest(): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(getGoogleIdOption(serverClientId = credentials.serverId))
            .build()
    }

    private fun getGoogleIdOption(serverClientId: String): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setServerClientId(serverClientId)
            .build()
    }
}