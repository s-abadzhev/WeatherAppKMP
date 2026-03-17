package ru.sergeyabadzhev.weatherappkmp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class ForecastResponseDTO(
    val daily: DailyDTO,
    val hourly: HourlyDTO
)

@kotlinx.serialization.Serializable
data class DailyDTO(
    val time: List<String>,
    @SerialName("temperature_2m_max") val temperature2mMax: List<Double>,
    @SerialName("temperature_2m_min") val temperature2mMin: List<Double>,
    @SerialName("precipitation_sum") val precipitationSum: List<Double>,
    @SerialName("precipitation_probability_max") val precipitationProbabilityMax: List<Int>,
    @SerialName("weather_code") val weatherCode: List<Int>
)

@Serializable
data class HourlyDTO(
    val time: List<String>,
    @SerialName("temperature_2m") val temperature2m: List<Double>,
    @SerialName("precipitation_probability") val precipitationProbability: List<Int>,
    @SerialName("wind_speed_10m") val windSpeed10m: List<Double>,
    @SerialName("weather_code") val weatherCode: List<Int>
)