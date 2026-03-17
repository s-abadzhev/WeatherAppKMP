package ru.sergeyabadzhev.weatherappkmp.core.location

import kotlinx.coroutines.CancellationException

expect class LocationProvider {
    @Throws(LocationError::class, CancellationException::class)
    suspend fun getCurrentLocation(): Coordinates
}