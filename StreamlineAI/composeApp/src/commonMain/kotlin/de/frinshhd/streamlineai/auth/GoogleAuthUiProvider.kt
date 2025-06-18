package de.frinshhd.streamlineai.auth

import de.frinshhd.streamlineai.auth.models.GoogleUser

interface GoogleAuthUiProvider {

    /**
     * Opens Sign In with Google UI,
     * @return returns GoogleUser
     */
    suspend fun signIn(): GoogleUser?
}