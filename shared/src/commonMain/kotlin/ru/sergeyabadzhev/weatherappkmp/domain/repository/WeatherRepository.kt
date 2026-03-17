package ru.sergeyabadzhev.weatherappkmp.domain.repository

import kotlinx.coroutines.CancellationException
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationError
import ru.sergeyabadzhev.weatherappkmp.core.network.NetworkError
import ru.sergeyabadzhev.weatherappkmp.domain.model.Forecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.HourlyForecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.Weather

data class ForecastResult(
    val daily: List<Forecast>,
    val hourly: List<HourlyForecast>
)
interface WeatherRepository {
    @Throws(NetworkError::class, LocationError::class, CancellationException::class)
    suspend fun fetchCurrentWeather(lat: Double, lon: Double): Weather
    @Throws(NetworkError::class, CancellationException::class)
    suspend fun fetchForecast(lat: Double, lon: Double): ForecastResult
}