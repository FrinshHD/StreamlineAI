package de.frinshhd.streamlineai.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import de.frinshhd.streamlineai.BuildConfig

class GoogleAuthenticator(
    private val context: Context,
    private val credentialManager: CredentialManager
) {

    suspend fun login(): String {
        Log.d("GoogleAuthenticator", "Package name: ${context.packageName}")
        try {
            val pm = context.packageManager
            val info = pm.getPackageInfo(context.packageName, android.content.pm.PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = if (android.os.Build.VERSION.SDK_INT >= 28) info.signingInfo?.apkContentsSigners else @Suppress("DEPRECATION") info.signatures
            signatures?.forEach { sig ->
                val sha1 = java.security.MessageDigest.getInstance("SHA1").digest(sig.toByteArray())
                val sha1String = sha1.joinToString(":") { b -> "%02X".format(b) }
                Log.d("GoogleAuthenticator", "SHA1: $sha1String")
            }
        } catch (e: Exception) {
            Log.e("GoogleAuthenticator", "Failed to print SHA1: ${e.message}", e)
        }
        Log.d("GoogleAuthenticator", "Starting Google login flow")
        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID_ANDROID)
                .setAutoSelectEnabled(false)
                .setFilterByAuthorizedAccounts(false)
                .build()

            Log.d("GoogleAuthenticator", "GoogleIdOption: serverClientId=${BuildConfig.GOOGLE_CLIENT_ID_ANDROID}, autoSelectEnabled=false, filterByAuthorizedAccounts=false")
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            Log.d("GoogleAuthenticator", "GetCredentialRequest: $request")
            val result = credentialManager.getCredential(context, request)
            Log.d("GoogleAuthenticator", "CredentialManager result: $result")
            val credential = result.credential
            Log.d("GoogleAuthenticator", "Credential class: ${credential::class.java.name}, credential: $credential")
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                Log.d("GoogleAuthenticator", "GoogleIdTokenCredential: $googleIdTokenCredential, idToken: $idToken")
                return idToken
            }
            Log.e("GoogleAuthenticator", "Unexpected credential type or missing idToken. Credential: $credential")
            return "Something went wrong"
        } catch (e: NoCredentialException) {
            Log.w("GoogleAuthenticator", "NoCredentialException: ${e.message}")
            Log.w("GoogleAuthenticator", Log.getStackTraceString(e))
            return "No email found in your device"
        } catch (e: GetCredentialException) {
            Log.e("GoogleAuthenticator", "GetCredentialException: ${e.message}", e)
            Log.e("GoogleAuthenticator", Log.getStackTraceString(e))
            Log.e("GoogleAuthenticator", "Exception class: ${e::class.java.name}, cause: ${e.cause}")
            if (e.message?.contains("cancelled", ignoreCase = true) == true) {
                return "Sign-in was cancelled by the user."
            }
            return e.message.toString()
        } catch (e: Exception) {
            Log.e("GoogleAuthenticator", "Exception: ${e.message}", e)
            Log.e("GoogleAuthenticator", Log.getStackTraceString(e))
            Log.e("GoogleAuthenticator", "Exception class: ${e::class.java.name}, cause: ${e.cause}")
            return e.message.toString()
        }


    }

    fun logout() {
        // No Firebase, so nothing to do here
    }

}