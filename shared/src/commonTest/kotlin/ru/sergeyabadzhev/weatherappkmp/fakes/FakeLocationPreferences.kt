package ru.sergeyabadzhev.weatherappkmp.fakes

import ru.sergeyabadzhev.weatherappkmp.core.storage.LastLocation
import ru.sergeyabadzhev.weatherappkmp.core.storage.LocationPreferencesInterface

class FakeLocationPreferences(
    private var lastLocation: LastLocation = LastLocation.DeviceLocation
) : LocationPreferencesInterface {

    var saveDeviceLocationCallCount = 0
    var saveSelectedCityCallCount = 0
    var lastSavedLat: Double? = null
    var lastSavedLon: Double? = null
    var lastSavedCityName: String? = null

    override suspend fun saveDeviceLocation() {
        saveDeviceLocationCallCount++
        lastLocation = LastLocation.DeviceLocation
    }

    override suspend fun saveSelectedCity(lat: Double, lon: Double, cityName: String) {
        saveSelectedCityCallCount++
        lastSavedLat = lat
        lastSavedLon = lon
        lastSavedCityName = cityName
        lastLocation = LastLocation.SelectedCity(lat, lon, cityName)
    }

    override suspend fun getLastLocation(): LastLocation = lastLocation
}
