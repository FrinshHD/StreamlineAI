package de.frinshhd.streamlineai.data

import de.frinshhd.streamlineai.models.User


object UserRepository {
    private val users = mutableMapOf<String, User>()
    private val tokens = mutableMapOf<String, String>() // token -> userId

    fun saveUser(user: User) {
        users[user.id] = user
    }

    fun getUser(id: String): User? = users[id]

    fun getAllUsers(): List<User> = users.values.toList()

    fun saveToken(token: String, userId: String) {
        tokens[token] = userId
    }

    fun getUserIdByToken(token: String): String? = tokens[token]
}
