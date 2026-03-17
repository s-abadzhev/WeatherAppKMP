package ru.sergeyabadzhev.weatherappkmp.domain.repository

import kotlinx.coroutines.CancellationException
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationError
import ru.sergeyabadzhev.weatherappkmp.domain.model.City

interface CityRepository {
    @Throws(LocationError::class, CancellationException::class)
    suspend fun searchCity(query: String): List<City>
    @Throws(LocationError::class, CancellationException::class)
    suspend fun reverseGeocode(lat: Double, lon: Double): City
}