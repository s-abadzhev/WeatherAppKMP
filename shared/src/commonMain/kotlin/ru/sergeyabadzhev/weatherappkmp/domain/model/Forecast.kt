package ru.sergeyabadzhev.weatherappkmp.domain.model

import kotlinx.datetime.LocalDate

data class Forecast(
    val date: LocalDate,
    val tempMin: Double,
    val tempMax: Double,
    val condition: WeatherCondition,
    val precipitationProbability: Int,
    val precipitationSum: Double
)