package ru.sergeyabadzhev.weatherappkmp.core.storage

interface LocationPreferencesInterface {
    suspend fun saveDeviceLocation()
    suspend fun saveSelectedCity(lat: Double, lon: Double, cityName: String)
    suspend fun getLastLocation(): LastLocation
}
