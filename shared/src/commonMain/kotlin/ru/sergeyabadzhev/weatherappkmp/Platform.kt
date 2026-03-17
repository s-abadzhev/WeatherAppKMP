package ru.sergeyabadzhev.weatherappkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform