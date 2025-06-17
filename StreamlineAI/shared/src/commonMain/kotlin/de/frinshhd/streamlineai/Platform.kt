package de.frinshhd.streamlineai

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform