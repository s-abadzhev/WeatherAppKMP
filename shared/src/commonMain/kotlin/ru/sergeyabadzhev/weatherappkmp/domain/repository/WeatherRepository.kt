package ru.sergeyabadzhev.weatherappkmp.domain.repository

import ru.sergeyabadzhev.weatherappkmp.domain.model.Forecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.HourlyForecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.Weather

interface WeatherRepository {
    suspend fun fetchCurrentWeather(lat: Double, lon: Double): Weather
    suspend fun fetchForecast(lat: Double, lon: Double): Pair<List<Forecast>, List<HourlyForecast>>
}