package ru.sergeyabadzhev.weatherappkmp.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.sergeyabadzhev.weatherappkmp.core.storage.CityStorageInterface
import ru.sergeyabadzhev.weatherappkmp.domain.model.City

class FakeCityStorage(initial: List<City> = emptyList()) : CityStorageInterface {

    private val _savedCities = MutableStateFlow(initial)
    override val savedCities: Flow<List<City>> = _savedCities

    var saveCityCallCount = 0
    var removeCityCallCount = 0
    var lastSavedCity: City? = null
    var lastRemovedCity: City? = null

    override suspend fun saveCity(city: City) {
        saveCityCallCount++
        lastSavedCity = city
        _savedCities.update { it + city }
    }

    override suspend fun removeCity(city: City) {
        removeCityCallCount++
        lastRemovedCity = city
        _savedCities.update { list -> list.filter { it.id != city.id } }
    }
}
