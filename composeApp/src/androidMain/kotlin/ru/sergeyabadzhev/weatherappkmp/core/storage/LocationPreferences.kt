package ru.sergeyabadzhev.weatherappkmp.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "location_prefs")

class LocationPreferences(private val context: Context) : LocationPreferencesInterface {

    companion object {
        private val USE_DEVICE_LOCATION = booleanPreferencesKey("use_device_location")
        private val SAVED_LAT = doublePreferencesKey("saved_lat")
        private val SAVED_LON = doublePreferencesKey("saved_lon")
        private val SAVED_CITY_NAME = stringPreferencesKey("saved_city_name")
    }

    override suspend fun saveDeviceLocation() {
        context.dataStore.edit { prefs ->
            prefs[USE_DEVICE_LOCATION] = true
        }
    }

    override suspend fun saveSelectedCity(lat: Double, lon: Double, cityName: String) {
        context.dataStore.edit { prefs ->
            prefs[USE_DEVICE_LOCATION] = false
            prefs[SAVED_LAT] = lat
            prefs[SAVED_LON] = lon
            prefs[SAVED_CITY_NAME] = cityName
        }
    }

    override suspend fun getLastLocation(): LastLocation {
        val prefs = context.dataStore.data.first()
        val useDeviceLocation = prefs[USE_DEVICE_LOCATION] ?: true

        return if (useDeviceLocation) {
            LastLocation.DeviceLocation
        } else {
            val lat = prefs[SAVED_LAT]
            val lon = prefs[SAVED_LON]
            val name = prefs[SAVED_CITY_NAME]
            if (lat != null && lon != null && name != null) {
                LastLocation.SelectedCity(lat, lon, name)
            } else {
                LastLocation.DeviceLocation
            }
        }
    }
}

