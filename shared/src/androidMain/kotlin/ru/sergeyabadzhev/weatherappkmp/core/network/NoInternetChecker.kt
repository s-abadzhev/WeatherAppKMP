package ru.sergeyabadzhev.weatherappkmp.core.network

internal actual fun Exception.isNoInternetException(): Boolean =
    this is java.net.UnknownHostException
