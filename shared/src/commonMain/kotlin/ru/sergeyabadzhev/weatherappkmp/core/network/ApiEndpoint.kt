package ru.sergeyabadzhev.weatherappkmp.core.network

sealed class ApiEndpoint {
    data class CurrentWeather(val lat: Double, val lon: Double) : ApiEndpoint()
    data class Forecast(val lat: Double, val lon: Double) : ApiEndpoint()

    val url: String
        get() = when (this) {
            is CurrentWeather -> buildString {
                append("https://api.open-meteo.com/v1/forecast")
                append("?latitude=$lat&longitude=$lon")
                append("&current=temperature_2m,relative_humidity_2m,apparent_temperature")
                append(",wind_speed_10m,wind_direction_10m,uv_index,visibility,weather_code")
                append("&timezone=auto")
            }
            is Forecast -> buildString {
                append("https://api.open-meteo.com/v1/forecast")
                append("?latitude=$lat&longitude=$lon")
                append("&daily=temperature_2m_max,temperature_2m_min")
                append(",precipitation_sum,precipitation_probability_max,weather_code")
                append("&hourly=temperature_2m,precipitation_probability,wind_speed_10m,weather_code")
                append("&forecast_days=7&timezone=auto")
            }
        }
}