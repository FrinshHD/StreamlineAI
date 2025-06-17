package de.frinshhd.streamlineai.utils

import io.github.cdimascio.dotenv.dotenv

private val dotenv = dotenv()

object Env {
    operator fun get(key: String): String? = dotenv[key]
    fun get(key: String, default: String): String = dotenv.get(key, default)
    fun entries(): Map<String, String> = dotenv.entries().associate { it.key to it.value }
}
