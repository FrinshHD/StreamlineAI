package de.frinshhd.steamlineai

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform