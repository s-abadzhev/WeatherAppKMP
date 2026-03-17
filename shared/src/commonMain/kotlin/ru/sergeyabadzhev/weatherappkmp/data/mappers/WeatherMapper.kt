package ru.sergeyabadzhev.weatherappkmp.data.mappers

import kotlin.time.Clock
import ru.sergeyabadzhev.weatherappkmp.data.dto.CurrentWeatherResponseDTO
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import ru.sergeyabadzhev.weatherappkmp.domain.model.Weather

object WeatherMapper {

    fun toDomain(response: CurrentWeatherResponseDTO, city: City): Weather {
        val dto = response.current
        return Weather(
            city = city,
            temperature = dto.temperature2m,
            feelsLike = dto.apparentTemperature,
            humidity = dto.relativeHumidity2m,
            windSpeed = dto.windSpeed10m,
            windDirection = dto.windDirection10m,
            uvIndex = dto.uvIndex,
            visibility = dto.visibility,
            condition = WeatherConditionMapper.toDomain(dto.weatherCode),
            updatedAt = Clock.System.now()
        )
    }
}