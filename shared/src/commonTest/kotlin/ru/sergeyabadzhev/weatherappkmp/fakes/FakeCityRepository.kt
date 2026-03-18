package ru.sergeyabadzhev.weatherappkmp.fakes

import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import ru.sergeyabadzhev.weatherappkmp.domain.repository.CityRepository

class FakeCityRepository : CityRepository {

    var searchResult: Result<List<City>> = Result.success(emptyList())
    var reverseGeocodeResult: Result<City> = Result.success(
        City(id = "geocoded-id", name = "Moscow", country = "Russia", latitude = 55.75, longitude = 37.62)
    )

    var searchCallCount = 0
    var lastSearchQuery: String? = null

    override suspend fun searchCity(query: String): List<City> {
        searchCallCount++
        lastSearchQuery = query
        return searchResult.getOrThrow()
    }

    override suspend fun reverseGeocode(lat: Double, lon: Double): City {
        return reverseGeocodeResult.getOrThrow()
    }
}
