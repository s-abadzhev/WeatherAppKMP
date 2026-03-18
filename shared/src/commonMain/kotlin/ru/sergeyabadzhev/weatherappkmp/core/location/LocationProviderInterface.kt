package ru.sergeyabadzhev.weatherappkmp.core.location

import kotlinx.coroutines.CancellationException

interface LocationProviderInterface {
    @Throws(LocationError::class, CancellationException::class)
    suspend fun getCurrentLocation(): Coordinates
}
