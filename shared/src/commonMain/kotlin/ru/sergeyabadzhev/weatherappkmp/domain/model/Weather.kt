package ru.sergeyabadzhev.weatherappkmp.domain.model

import kotlinx.datetime.Instant

data class Weather(
    val city: City,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val windDirection: Double,
    val uvIndex: Double,
    val visibility: Double,
    val condition: WeatherCondition,
    val updatedAt: Instant
)