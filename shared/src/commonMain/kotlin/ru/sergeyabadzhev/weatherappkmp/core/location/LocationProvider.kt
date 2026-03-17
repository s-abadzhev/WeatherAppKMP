package ru.sergeyabadzhev.weatherappkmp.core.location

expect class LocationProvider {
    suspend fun getCurrentLocation(): Coordinates
}