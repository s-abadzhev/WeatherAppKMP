package ru.sergeyabadzhev.weatherappkmp.core.location

import kotlinx.coroutines.CancellationException
import ru.sergeyabadzhev.weatherappkmp.domain.model.City

expect class GeocoderService {
    @Throws(LocationError::class, CancellationException::class)
    suspend fun reverseGeocode(lat: Double, lon: Double): City
    @Throws(LocationError::class, CancellationException::class)
    suspend fun searchCity(query: String): List<City>
}