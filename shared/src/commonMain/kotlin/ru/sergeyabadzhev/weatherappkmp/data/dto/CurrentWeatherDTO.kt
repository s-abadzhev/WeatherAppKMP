package ru.sergeyabadzhev.weatherappkmp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class CurrentWeatherResponseDTO(
    val current: CurrentWeatherDTO
)

@Serializable
data class CurrentWeatherDTO(
    @SerialName("temperature_2m") val temperature2m: Double,
    @SerialName("relative_humidity_2m") val relativeHumidity2m: Int,
    @SerialName("apparent_temperature") val apparentTemperature: Double,
    @SerialName("wind_speed_10m") val windSpeed10m: Double,
    @SerialName("wind_direction_10m") val windDirection10m: Double,
    @SerialName("uv_index") val uvIndex: Double,
    val visibility: Double,
    @SerialName("weather_code") val weatherCode: Int
)