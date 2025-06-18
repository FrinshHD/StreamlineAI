package de.frinshhd.streamlineai.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val idToken: String,
    val email: String,
    val name: String,
    val displayName: String = name,
    val pictureUrl: String?
)