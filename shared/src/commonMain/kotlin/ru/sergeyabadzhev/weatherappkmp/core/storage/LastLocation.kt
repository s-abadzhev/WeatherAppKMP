package ru.sergeyabadzhev.weatherappkmp.core.storage

sealed class LastLocation {
    data object DeviceLocation : LastLocation()
    data class SelectedCity(val lat: Double, val lon: Double, val name: String) : LastLocation()
}
