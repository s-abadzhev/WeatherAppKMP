package ru.sergeyabadzhev.weatherappkmp.domain.repository

import ru.sergeyabadzhev.weatherappkmp.domain.model.City

interface CityRepository {
    suspend fun searchCity(query: String): List<City>
    suspend fun reverseGeocode(lat: Double, lon: Double): City
}