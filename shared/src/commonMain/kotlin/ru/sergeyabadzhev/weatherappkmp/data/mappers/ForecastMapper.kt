package ru.sergeyabadzhev.weatherappkmp.data.mappers

import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import ru.sergeyabadzhev.weatherappkmp.data.dto.ForecastResponseDTO
import ru.sergeyabadzhev.weatherappkmp.domain.model.Forecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.HourlyForecast
import kotlin.runCatching

object ForecastMapper {

    fun toDailyDomain(response: ForecastResponseDTO): List<Forecast> {
        val daily = response.daily
        return daily.time.indices.mapNotNull { index ->
            runCatching {
                Forecast(
                    date = LocalDate.parse(daily.time[index]),
                    tempMin = daily.temperature2mMin[index],
                    tempMax = daily.temperature2mMax[index],
                    condition = WeatherConditionMapper.toDomain(daily.weatherCode[index]),
                    precipitationProbability = daily.precipitationProbabilityMax[index],
                    precipitationSum = daily.precipitationSum[index]
                )
            }.getOrNull()
        }
    }

    fun toHourlyDomain(response: ForecastResponseDTO): List<HourlyForecast> {
        val hourly = response.hourly
        val now = Clock.System.now()
        val next24Hours = now.plus(24, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())

        return hourly.time.indices.mapNotNull { index ->
            runCatching {
                val instant = LocalDateTime.parse(hourly.time[index])
                    .toInstant(TimeZone.currentSystemDefault())

                if (instant !in now..next24Hours) return@mapNotNull null

                HourlyForecast(
                    date = instant,
                    temperature = hourly.temperature2m[index],
                    condition = WeatherConditionMapper.toDomain(hourly.weatherCode[index]),
                    precipitationProbability = hourly.precipitationProbability[index],
                    windSpeed = hourly.windSpeed10m[index]
                )
            }.getOrNull()
        }
    }
}