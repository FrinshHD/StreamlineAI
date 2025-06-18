package de.frinshhd.streamlineai.auth

import de.frinshhd.streamlineai.models.User

interface GoogleAuthUiProvider {

    /**
     * Opens Sign In with Google UI,
     * @return returns User
     */
    suspend fun signIn(): User?
}