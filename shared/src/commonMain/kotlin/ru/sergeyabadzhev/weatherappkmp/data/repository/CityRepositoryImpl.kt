package ru.sergeyabadzhev.weatherappkmp.data.repository

import ru.sergeyabadzhev.weatherappkmp.core.location.GeocoderService
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import ru.sergeyabadzhev.weatherappkmp.domain.repository.CityRepository

class CityRepositoryImpl(
    private val geocoderService: GeocoderService
) : CityRepository {

    override suspend fun searchCity(query: String): List<City> {
        return geocoderService.searchCity(query)
    }

    override suspend fun reverseGeocode(lat: Double, lon: Double): City {
        return geocoderService.reverseGeocode(lat, lon)
    }
}