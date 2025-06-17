package de.frinshhd.streamlineai.auth

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

actual object AuthManager {
    actual fun loginWithGoogle(context: Any) {
        val ctx = context as Context
        val authUrl = "http://wiwaxia.fritz.box:8080/login" // Android emulator uses 10.0.2.2 for localhost
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(ctx, Uri.parse(authUrl))
    }
}