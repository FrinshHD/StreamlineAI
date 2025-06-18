package de.frinshhd.streamlineai.auth

import androidx.compose.runtime.Composable

interface GoogleAuthProvider {

    /**
     * Provides the UI for Google authentication.
     */
    @Composable
    fun getUiProvider(): GoogleAuthUiProvider

    suspend fun signOut()
}