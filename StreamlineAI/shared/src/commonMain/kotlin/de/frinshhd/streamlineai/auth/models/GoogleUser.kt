package de.frinshhd.streamlineai.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class GoogleUser(
    val id: String = "1", // managed by your backend or app
    val idToken: String,
    val displayName: String = "",
    val email: String = "",
    val profilePicUrl: String? = null,
)