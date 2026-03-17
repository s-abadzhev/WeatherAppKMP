package ru.sergeyabadzhev.weatherappkmp.core.location

import ru.sergeyabadzhev.weatherappkmp.domain.model.City

expect class GeocoderService {
    suspend fun reverseGeocode(lat: Double, lon: Double): City
    suspend fun searchCity(query: String): List<City>
}