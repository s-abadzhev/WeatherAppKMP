package ru.sergeyabadzhev.weatherappkmp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.serialization.json.Json

class NetworkClient {

    @PublishedApi internal val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = if (isDebugBuild) LogLevel.BODY else LogLevel.NONE
        }
    }

    suspend inline fun <reified T> request(endpoint: ApiEndpoint): T {
        return try {
            client.get(endpoint.url).body()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    @PublishedApi internal fun mapException(e: Exception): NetworkError = when {
        e is ConnectTimeoutException || e is SocketTimeoutException -> NetworkError.Timeout()
        e.isNoInternetException() -> NetworkError.NoInternetConnection()
        else -> NetworkError.Unknown(e)
    }

    fun close() {
        client.close()
    }
}
