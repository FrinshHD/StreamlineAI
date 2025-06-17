package de.frinshhd.streamlineai.auth

actual object AuthManager {
    actual fun loginWithGoogle(context: Any) {
        throw UnsupportedOperationException("Google login is not available on JVM/server")
    }
}