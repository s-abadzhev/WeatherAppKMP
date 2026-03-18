package ru.sergeyabadzhev.weatherappkmp.core.storage

import kotlinx.coroutines.flow.Flow
import ru.sergeyabadzhev.weatherappkmp.domain.model.City

interface CityStorageInterface {
    val savedCities: Flow<List<City>>
    suspend fun saveCity(city: City)
    suspend fun removeCity(city: City)
}
