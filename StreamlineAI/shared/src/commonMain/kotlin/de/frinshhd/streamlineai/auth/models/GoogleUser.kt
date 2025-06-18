package de.frinshhd.streamlineai.auth.models

data class GoogleUser(
    val idToken: String,
    val displayName: String = "",
    val profilePicUrl: String? = null,
)