package de.frinshhd.streamlineai.util

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import de.frinshhd.streamlineai.utils.Env

object GoogleTokenVerifierUtil {
    private val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
        .setAudience(listOf(Env.get("GOOGLE_CLIENT_ID", "")))
        .build()

    fun verify(idTokenString: String): GoogleIdToken.Payload? {
        val idToken = verifier.verify(idTokenString)
        return idToken?.payload
    }
}

