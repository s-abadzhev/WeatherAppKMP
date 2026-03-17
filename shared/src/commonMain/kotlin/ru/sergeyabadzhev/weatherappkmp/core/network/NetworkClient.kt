package ru.sergeyabadzhev.weatherappkmp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NetworkClient {

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

    suspend inline fun <reified T> request(endpoint: ApiEndpoint): T {
        return try {
            client.get(endpoint.url).body()
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    fun mapException(e: Exception): NetworkError {
        return when {
            e.message?.contains("Unable to resolve host") == true -> NetworkError.NoInternetConnection()
            e.message?.contains("timeout") == true -> NetworkError.Timeout()
            else -> NetworkError.Unknown(e)
        }
    }

    fun close() {
        client.close()
    }
}