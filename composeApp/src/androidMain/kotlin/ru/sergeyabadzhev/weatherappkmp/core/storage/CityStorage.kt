package ru.sergeyabadzhev.weatherappkmp.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import kotlin.collections.emptyList
import kotlinx.serialization.json.Json

private val Context.cityDataStore: DataStore<Preferences> by preferencesDataStore(name = "city_prefs")

class CityStorage(private val context: Context) : CityStorageInterface {

    companion object {
        private val SAVED_CITIES = stringPreferencesKey("saved_cities")
    }

    override val savedCities: Flow<List<City>> = context.cityDataStore.data.map { prefs ->
        val json = prefs[SAVED_CITIES] ?: return@map emptyList()
        try {
            Json.decodeFromString<List<CityStorageDTO>>(json).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun saveCity(city: City) {
        context.cityDataStore.edit { prefs ->
            val current = prefs[SAVED_CITIES]?.let {
                Json.decodeFromString<List<CityStorageDTO>>(it)
            } ?: emptyList()

            if (current.any { it.id == city.id }) return@edit

            val updated = current + CityStorageDTO.fromDomain(city)
            prefs[SAVED_CITIES] = Json.encodeToString(updated)
        }
    }

    override suspend fun removeCity(city: City) {
        context.cityDataStore.edit { prefs ->
            val current = prefs[SAVED_CITIES]?.let {
                Json.decodeFromString<List<CityStorageDTO>>(it)
            } ?: emptyList()

            val updated = current.filter { it.id != city.id }
            prefs[SAVED_CITIES] = Json.encodeToString(updated)
        }
    }
}

