package ru.sergeyabadzhev.weatherappkmp.core.network

import io.ktor.client.engine.darwin.DarwinHttpRequestException

private const val NSURLErrorNotConnectedToInternet = -1009L
private const val NSURLErrorCannotFindHost = -1003L
private const val NSURLErrorNetworkConnectionLost = -1005L
private const val NSURLErrorCannotConnectToHost = -1004L

internal actual fun Exception.isNoInternetException(): Boolean =
    this is DarwinHttpRequestException && origin.code in setOf(
        NSURLErrorNotConnectedToInternet,
        NSURLErrorCannotFindHost,
        NSURLErrorNetworkConnectionLost,
        NSURLErrorCannotConnectToHost
    )
