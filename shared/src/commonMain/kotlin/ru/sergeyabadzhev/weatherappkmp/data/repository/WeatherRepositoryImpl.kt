package ru.sergeyabadzhev.weatherappkmp.data.repository

import kotlinx.coroutines.CancellationException
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationError
import ru.sergeyabadzhev.weatherappkmp.core.network.ApiEndpoint
import ru.sergeyabadzhev.weatherappkmp.core.network.NetworkClient
import ru.sergeyabadzhev.weatherappkmp.core.network.NetworkError
import ru.sergeyabadzhev.weatherappkmp.data.dto.CurrentWeatherResponseDTO
import ru.sergeyabadzhev.weatherappkmp.data.dto.ForecastResponseDTO
import ru.sergeyabadzhev.weatherappkmp.data.mappers.ForecastMapper
import ru.sergeyabadzhev.weatherappkmp.data.mappers.WeatherMapper
import ru.sergeyabadzhev.weatherappkmp.domain.model.Forecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.HourlyForecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.Weather
import ru.sergeyabadzhev.weatherappkmp.domain.repository.CityRepository
import ru.sergeyabadzhev.weatherappkmp.domain.repository.ForecastResult
import ru.sergeyabadzhev.weatherappkmp.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val networkClient: NetworkClient,
    private val cityRepository: CityRepository
) : WeatherRepository {

    @Throws(NetworkError::class, LocationError::class, CancellationException::class)
    override suspend fun fetchCurrentWeather(lat: Double, lon: Double): Weather {
        val city = cityRepository.reverseGeocode(lat, lon)
        val response = networkClient.request<CurrentWeatherResponseDTO>(
            ApiEndpoint.CurrentWeather(lat, lon)
        )
        return WeatherMapper.toDomain(response, city)
    }

    @Throws(NetworkError::class, CancellationException::class)
    override suspend fun fetchForecast(lat: Double, lon: Double): ForecastResult {
        val response = networkClient.request<ForecastResponseDTO>(ApiEndpoint.Forecast(lat, lon))
        return ForecastResult(
            daily = ForecastMapper.toDailyDomain(response),
            hourly = ForecastMapper.toHourlyDomain(response)
        )
    }
}