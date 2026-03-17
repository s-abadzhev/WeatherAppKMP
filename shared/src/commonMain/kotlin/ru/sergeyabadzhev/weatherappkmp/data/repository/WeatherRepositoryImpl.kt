package ru.sergeyabadzhev.weatherappkmp.data.repository

import ru.sergeyabadzhev.weatherappkmp.core.network.ApiEndpoint
import ru.sergeyabadzhev.weatherappkmp.core.network.NetworkClient
import ru.sergeyabadzhev.weatherappkmp.data.dto.CurrentWeatherResponseDTO
import ru.sergeyabadzhev.weatherappkmp.data.dto.ForecastResponseDTO
import ru.sergeyabadzhev.weatherappkmp.data.mappers.ForecastMapper
import ru.sergeyabadzhev.weatherappkmp.data.mappers.WeatherMapper
import ru.sergeyabadzhev.weatherappkmp.domain.model.Forecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.HourlyForecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.Weather
import ru.sergeyabadzhev.weatherappkmp.domain.repository.CityRepository
import ru.sergeyabadzhev.weatherappkmp.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val networkClient: NetworkClient,
    private val cityRepository: CityRepository
) : WeatherRepository {

    override suspend fun fetchCurrentWeather(lat: Double, lon: Double): Weather {
        val city = cityRepository.reverseGeocode(lat, lon)
        val response = networkClient.request<CurrentWeatherResponseDTO>(
            ApiEndpoint.CurrentWeather(lat, lon)
        )
        return WeatherMapper.toDomain(response, city)
    }

    override suspend fun fetchForecast(
        lat: Double,
        lon: Double
    ): Pair<List<Forecast>, List<HourlyForecast>> {
        val response = networkClient.request<ForecastResponseDTO>(
            ApiEndpoint.Forecast(lat, lon)
        )
        return Pair(
            ForecastMapper.toDailyDomain(response),
            ForecastMapper.toHourlyDomain(response)
        )
    }
}