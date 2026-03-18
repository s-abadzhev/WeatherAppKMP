package ru.sergeyabadzhev.weatherappkmp.fakes

import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import ru.sergeyabadzhev.weatherappkmp.domain.model.Forecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.HourlyForecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.Weather
import ru.sergeyabadzhev.weatherappkmp.domain.model.WeatherCondition
import ru.sergeyabadzhev.weatherappkmp.domain.repository.ForecastResult
import ru.sergeyabadzhev.weatherappkmp.domain.repository.WeatherRepository
import kotlin.time.Clock

class FakeWeatherRepository : WeatherRepository {

    var weatherResult: Result<Weather> = Result.success(defaultWeather())
    var forecastResult: Result<ForecastResult> = Result.success(ForecastResult(emptyList(), emptyList()))

    var fetchWeatherCallCount = 0
    var fetchForecastCallCount = 0
    var lastFetchedLat: Double? = null
    var lastFetchedLon: Double? = null

    override suspend fun fetchCurrentWeather(lat: Double, lon: Double): Weather {
        fetchWeatherCallCount++
        lastFetchedLat = lat
        lastFetchedLon = lon
        return weatherResult.getOrThrow()
    }

    override suspend fun fetchForecast(lat: Double, lon: Double): ForecastResult {
        fetchForecastCallCount++
        return forecastResult.getOrThrow()
    }

    companion object {
        fun defaultWeather(
            cityName: String = "Moscow",
            lat: Double = 55.75,
            lon: Double = 37.62
        ) = Weather(
            city = City(id = "test-id", name = cityName, country = "Russia", latitude = lat, longitude = lon),
            temperature = 20.0,
            feelsLike = 18.5,
            humidity = 60,
            windSpeed = 5.0,
            windDirection = 180.0,
            uvIndex = 3.0,
            visibility = 10000.0,
            condition = WeatherCondition.CLEAR_SKY,
            updatedAt = Clock.System.now()
        )
    }
}
