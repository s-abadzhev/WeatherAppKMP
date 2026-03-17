package ru.sergeyabadzhev.weatherappkmp.core.network

sealed class NetworkError : Exception() {
    class InvalidUrl : NetworkError()
    class NoInternetConnection : NetworkError()
    class Timeout : NetworkError()
    data class ServerError(val statusCode: Int) : NetworkError()
    data class DecodingError(override val cause: Throwable) : NetworkError()
    data class Unknown(override val cause: Throwable) : NetworkError()
}