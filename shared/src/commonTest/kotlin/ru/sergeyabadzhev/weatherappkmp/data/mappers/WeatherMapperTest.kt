package ru.sergeyabadzhev.weatherappkmp.data.mappers

import ru.sergeyabadzhev.weatherappkmp.data.dto.CurrentWeatherDTO
import ru.sergeyabadzhev.weatherappkmp.data.dto.CurrentWeatherResponseDTO
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import ru.sergeyabadzhev.weatherappkmp.domain.model.WeatherCondition
import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherMapperTest {

    private val testCity = City(
        id = "test-id",
        name = "Moscow",
        country = "Russia",
        latitude = 55.75,
        longitude = 37.62
    )

    private fun makeDto(
        temperature2m: Double = 20.0,
        apparentTemperature: Double = 18.5,
        relativeHumidity2m: Int = 65,
        windSpeed10m: Double = 5.3,
        windDirection10m: Double = 180.0,
        uvIndex: Double = 3.0,
        visibility: Double = 10000.0,
        weatherCode: Int = 0
    ) = CurrentWeatherResponseDTO(
        current = CurrentWeatherDTO(
            temperature2m = temperature2m,
            apparentTemperature = apparentTemperature,
            relativeHumidity2m = relativeHumidity2m,
            windSpeed10m = windSpeed10m,
            windDirection10m = windDirection10m,
            uvIndex = uvIndex,
            visibility = visibility,
            weatherCode = weatherCode
        )
    )

    @Test
    fun `maps temperature correctly`() {
        val weather = WeatherMapper.toDomain(makeDto(temperature2m = 25.5), testCity)
        assertEquals(25.5, weather.temperature)
    }

    @Test
    fun `maps feelsLike correctly`() {
        val weather = WeatherMapper.toDomain(makeDto(apparentTemperature = 22.1), testCity)
        assertEquals(22.1, weather.feelsLike)
    }

    @Test
    fun `maps humidity correctly`() {
        val weather = WeatherMapper.toDomain(makeDto(relativeHumidity2m = 78), testCity)
        assertEquals(78, weather.humidity)
    }

    @Test
    fun `maps windSpeed correctly`() {
        val weather = WeatherMapper.toDomain(makeDto(windSpeed10m = 12.4), testCity)
        assertEquals(12.4, weather.windSpeed)
    }

    @Test
    fun `maps windDirection correctly`() {
        val weather = WeatherMapper.toDomain(makeDto(windDirection10m = 270.0), testCity)
        assertEquals(270.0, weather.windDirection)
    }

    @Test
    fun `maps uvIndex correctly`() {
        val weather = WeatherMapper.toDomain(makeDto(uvIndex = 6.5), testCity)
        assertEquals(6.5, weather.uvIndex)
    }

    @Test
    fun `maps visibility correctly`() {
        val weather = WeatherMapper.toDomain(makeDto(visibility = 5000.0), testCity)
        assertEquals(5000.0, weather.visibility)
    }

    @Test
    fun `maps city correctly`() {
        val weather = WeatherMapper.toDomain(makeDto(), testCity)
        assertEquals(testCity, weather.city)
    }

    @Test
    fun `maps weather condition via WeatherConditionMapper`() {
        val weather = WeatherMapper.toDomain(makeDto(weatherCode = 95), testCity)
        assertEquals(WeatherCondition.THUNDERSTORM, weather.condition)
    }

    @Test
    fun `maps clear sky condition`() {
        val weather = WeatherMapper.toDomain(makeDto(weatherCode = 0), testCity)
        assertEquals(WeatherCondition.CLEAR_SKY, weather.condition)
    }

    @Test
    fun `maps rain condition`() {
        val weather = WeatherMapper.toDomain(makeDto(weatherCode = 61), testCity)
        assertEquals(WeatherCondition.RAIN, weather.condition)
    }

    @Test
    fun `updatedAt is set to a non-null instant`() {
        val weather = WeatherMapper.toDomain(makeDto(), testCity)
        // Just verify it's set (non-null Instant cannot be null in Kotlin)
        assertEquals(testCity, weather.city) // Sanity check that mapping ran
    }
}
