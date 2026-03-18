package ru.sergeyabadzhev.weatherappkmp.data.mappers

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.sergeyabadzhev.weatherappkmp.data.dto.DailyDTO
import ru.sergeyabadzhev.weatherappkmp.data.dto.ForecastResponseDTO
import ru.sergeyabadzhev.weatherappkmp.data.dto.HourlyDTO
import ru.sergeyabadzhev.weatherappkmp.domain.model.WeatherCondition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

class ForecastMapperTest {

    private fun makeDailyDto(
        times: List<String> = listOf("2026-03-18", "2026-03-19", "2026-03-20"),
        weatherCodes: List<Int> = listOf(0, 61, 95),
        tempMax: List<Double> = listOf(22.0, 18.0, 15.0),
        tempMin: List<Double> = listOf(10.0, 9.0, 7.0),
        precipSum: List<Double> = listOf(0.0, 5.2, 0.8),
        precipProbMax: List<Int> = listOf(0, 70, 40)
    ) = ForecastResponseDTO(
        daily = DailyDTO(
            time = times,
            weatherCode = weatherCodes,
            temperature2mMax = tempMax,
            temperature2mMin = tempMin,
            precipitationSum = precipSum,
            precipitationProbabilityMax = precipProbMax
        ),
        hourly = emptyHourlyDto()
    )

    private fun emptyHourlyDto() = HourlyDTO(
        time = emptyList(),
        temperature2m = emptyList(),
        precipitationProbability = emptyList(),
        windSpeed10m = emptyList(),
        weatherCode = emptyList()
    )

    // ---- Daily mapping ----

    @Test
    fun `toDailyDomain returns correct number of forecasts`() {
        val result = ForecastMapper.toDailyDomain(makeDailyDto())
        assertEquals(3, result.size)
    }

    @Test
    fun `toDailyDomain maps date correctly`() {
        val result = ForecastMapper.toDailyDomain(makeDailyDto())
        assertEquals("2026-03-18", result[0].date.toString())
        assertEquals("2026-03-19", result[1].date.toString())
    }

    @Test
    fun `toDailyDomain maps tempMax and tempMin correctly`() {
        val result = ForecastMapper.toDailyDomain(makeDailyDto())
        assertEquals(22.0, result[0].tempMax)
        assertEquals(10.0, result[0].tempMin)
    }

    @Test
    fun `toDailyDomain maps weather condition correctly`() {
        val result = ForecastMapper.toDailyDomain(makeDailyDto())
        assertEquals(WeatherCondition.CLEAR_SKY, result[0].condition)
        assertEquals(WeatherCondition.RAIN, result[1].condition)
        assertEquals(WeatherCondition.THUNDERSTORM, result[2].condition)
    }

    @Test
    fun `toDailyDomain maps precipitation correctly`() {
        val result = ForecastMapper.toDailyDomain(makeDailyDto())
        assertEquals(5.2, result[1].precipitationSum)
        assertEquals(70, result[1].precipitationProbability)
    }

    @Test
    fun `toDailyDomain skips entries with invalid date format`() {
        val dto = makeDailyDto(
            times = listOf("2026-03-18", "NOT_A_DATE", "2026-03-20"),
            weatherCodes = listOf(0, 0, 0),
            tempMax = listOf(20.0, 20.0, 20.0),
            tempMin = listOf(10.0, 10.0, 10.0),
            precipSum = listOf(0.0, 0.0, 0.0),
            precipProbMax = listOf(0, 0, 0)
        )
        val result = ForecastMapper.toDailyDomain(dto)
        // Invalid entry is skipped via runCatching
        assertEquals(2, result.size)
        assertEquals("2026-03-18", result[0].date.toString())
        assertEquals("2026-03-20", result[1].date.toString())
    }

    @Test
    fun `toDailyDomain returns empty list when no entries`() {
        val dto = makeDailyDto(
            times = emptyList(),
            weatherCodes = emptyList(),
            tempMax = emptyList(),
            tempMin = emptyList(),
            precipSum = emptyList(),
            precipProbMax = emptyList()
        )
        assertTrue(ForecastMapper.toDailyDomain(dto).isEmpty())
    }

    // ---- Hourly mapping ----

    @Test
    fun `toHourlyDomain includes entries within next 24 hours`() {
        val now = Clock.System.now()
        val tz = TimeZone.currentSystemDefault()

        val oneHourLater = (now + 1.hours).toLocalDateTime(tz).toString()
        val fiveHoursLater = (now + 5.hours).toLocalDateTime(tz).toString()
        val tenHoursLater = (now + 10.hours).toLocalDateTime(tz).toString()

        val dto = ForecastResponseDTO(
            daily = DailyDTO(
                time = emptyList(),
                weatherCode = emptyList(),
                temperature2mMax = emptyList(),
                temperature2mMin = emptyList(),
                precipitationSum = emptyList(),
                precipitationProbabilityMax = emptyList()
            ),
            hourly = HourlyDTO(
                time = listOf(oneHourLater, fiveHoursLater, tenHoursLater),
                temperature2m = listOf(15.0, 16.0, 14.0),
                precipitationProbability = listOf(10, 20, 30),
                windSpeed10m = listOf(3.0, 4.0, 5.0),
                weatherCode = listOf(0, 1, 61)
            )
        )

        val result = ForecastMapper.toHourlyDomain(dto)
        assertEquals(3, result.size)
        assertEquals(15.0, result[0].temperature)
        assertEquals(WeatherCondition.CLEAR_SKY, result[0].condition)
        assertEquals(WeatherCondition.RAIN, result[2].condition)
    }

    @Test
    fun `toHourlyDomain excludes entries in the past`() {
        val now = Clock.System.now()
        val tz = TimeZone.currentSystemDefault()

        val twoHoursAgo = (now - 2.hours).toLocalDateTime(tz).toString()
        val oneHourLater = (now + 1.hours).toLocalDateTime(tz).toString()

        val dto = ForecastResponseDTO(
            daily = DailyDTO(
                time = emptyList(),
                weatherCode = emptyList(),
                temperature2mMax = emptyList(),
                temperature2mMin = emptyList(),
                precipitationSum = emptyList(),
                precipitationProbabilityMax = emptyList()
            ),
            hourly = HourlyDTO(
                time = listOf(twoHoursAgo, oneHourLater),
                temperature2m = listOf(10.0, 20.0),
                precipitationProbability = listOf(0, 0),
                windSpeed10m = listOf(1.0, 2.0),
                weatherCode = listOf(0, 0)
            )
        )

        val result = ForecastMapper.toHourlyDomain(dto)
        assertEquals(1, result.size)
        assertEquals(20.0, result[0].temperature)
    }

    @Test
    fun `toHourlyDomain excludes entries beyond 24 hours`() {
        val now = Clock.System.now()
        val tz = TimeZone.currentSystemDefault()

        val oneHourLater = (now + 1.hours).toLocalDateTime(tz).toString()
        val twentyFiveHoursLater = (now + 25.hours).toLocalDateTime(tz).toString()

        val dto = ForecastResponseDTO(
            daily = DailyDTO(
                time = emptyList(),
                weatherCode = emptyList(),
                temperature2mMax = emptyList(),
                temperature2mMin = emptyList(),
                precipitationSum = emptyList(),
                precipitationProbabilityMax = emptyList()
            ),
            hourly = HourlyDTO(
                time = listOf(oneHourLater, twentyFiveHoursLater),
                temperature2m = listOf(15.0, 16.0),
                precipitationProbability = listOf(10, 20),
                windSpeed10m = listOf(3.0, 4.0),
                weatherCode = listOf(0, 0)
            )
        )

        val result = ForecastMapper.toHourlyDomain(dto)
        assertEquals(1, result.size)
        assertEquals(15.0, result[0].temperature)
    }

    @Test
    fun `toHourlyDomain maps all fields correctly`() {
        val now = Clock.System.now()
        val tz = TimeZone.currentSystemDefault()
        val twoHoursLater = (now + 2.hours).toLocalDateTime(tz).toString()

        val dto = ForecastResponseDTO(
            daily = DailyDTO(
                time = emptyList(),
                weatherCode = emptyList(),
                temperature2mMax = emptyList(),
                temperature2mMin = emptyList(),
                precipitationSum = emptyList(),
                precipitationProbabilityMax = emptyList()
            ),
            hourly = HourlyDTO(
                time = listOf(twoHoursLater),
                temperature2m = listOf(19.5),
                precipitationProbability = listOf(35),
                windSpeed10m = listOf(7.8),
                weatherCode = listOf(63)
            )
        )

        val result = ForecastMapper.toHourlyDomain(dto)
        assertEquals(1, result.size)
        assertEquals(19.5, result[0].temperature)
        assertEquals(35, result[0].precipitationProbability)
        assertEquals(7.8, result[0].windSpeed)
        assertEquals(WeatherCondition.RAIN, result[0].condition)
    }
}
