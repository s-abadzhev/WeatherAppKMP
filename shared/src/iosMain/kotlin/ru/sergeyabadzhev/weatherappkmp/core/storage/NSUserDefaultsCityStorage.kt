package ru.sergeyabadzhev.weatherappkmp.core.storage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults
import ru.sergeyabadzhev.weatherappkmp.domain.model.City

class NSUserDefaultsCityStorage : CityStorageInterface {

    private companion object {
        const val KEY = "saved_cities"
    }

    private val _savedCities = MutableStateFlow<List<City>>(emptyList())
    override val savedCities: Flow<List<City>> = _savedCities

    init {
        _savedCities.value = loadFromDefaults()
    }

    override suspend fun saveCity(city: City) {
        val current = _savedCities.value.toMutableList()
        if (current.any { it.id == city.id }) return
        current.add(city)
        persist(current)
        _savedCities.value = current
    }

    override suspend fun removeCity(city: City) {
        val updated = _savedCities.value.filter { it.id != city.id }
        persist(updated)
        _savedCities.value = updated
    }

    private fun persist(cities: List<City>) {
        val dtos = cities.map { CityStorageDTO.fromDomain(it) }
        val json = Json.encodeToString(dtos)
        NSUserDefaults.standardUserDefaults.setObject(json, KEY)
    }

    private fun loadFromDefaults(): List<City> {
        val json = NSUserDefaults.standardUserDefaults.stringForKey(KEY) ?: return emptyList()
        return try {
            Json.decodeFromString<List<CityStorageDTO>>(json).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
