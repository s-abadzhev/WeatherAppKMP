package ru.sergeyabadzhev.weatherappkmp.data.repository

import kotlinx.coroutines.CancellationException
import ru.sergeyabadzhev.weatherappkmp.core.location.GeocoderService
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationError
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import ru.sergeyabadzhev.weatherappkmp.domain.repository.CityRepository

class CityRepositoryImpl(
    private val geocoderService: GeocoderService
) : CityRepository {

    @Throws(LocationError::class, CancellationException::class, CancellationException::class)
    override suspend fun searchCity(query: String): List<City> {
        return geocoderService.searchCity(query)
    }

    @Throws(LocationError::class, CancellationException::class, CancellationException::class)
    override suspend fun reverseGeocode(lat: Double, lon: Double): City {
        return geocoderService.reverseGeocode(lat, lon)
    }
}