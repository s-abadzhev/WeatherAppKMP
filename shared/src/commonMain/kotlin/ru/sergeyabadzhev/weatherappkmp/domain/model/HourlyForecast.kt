package ru.sergeyabadzhev.weatherappkmp.domain.model

import kotlinx.datetime.Instant

data class HourlyForecast(
    val date: Instant,
    val temperature: Double,
    val condition: WeatherCondition,
    val precipitationProbability: Int,
    val windSpeed: Double
)