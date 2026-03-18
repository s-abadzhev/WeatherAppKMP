package ru.sergeyabadzhev.weatherappkmp.data.mappers

import ru.sergeyabadzhev.weatherappkmp.domain.model.WeatherCondition
import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherConditionMapperTest {

    // ---- Clear Sky ----

    @Test
    fun `code 0 maps to CLEAR_SKY`() {
        assertEquals(WeatherCondition.CLEAR_SKY, WeatherConditionMapper.toDomain(0))
    }

    // ---- Partly Cloudy ----

    @Test
    fun `code 1 maps to PARTLY_CLOUDY`() {
        assertEquals(WeatherCondition.PARTLY_CLOUDY, WeatherConditionMapper.toDomain(1))
    }

    @Test
    fun `code 2 maps to PARTLY_CLOUDY`() {
        assertEquals(WeatherCondition.PARTLY_CLOUDY, WeatherConditionMapper.toDomain(2))
    }

    // ---- Overcast ----

    @Test
    fun `code 3 maps to OVERCAST`() {
        assertEquals(WeatherCondition.OVERCAST, WeatherConditionMapper.toDomain(3))
    }

    // ---- Fog ----

    @Test
    fun `code 45 maps to FOG`() {
        assertEquals(WeatherCondition.FOG, WeatherConditionMapper.toDomain(45))
    }

    @Test
    fun `code 48 maps to FOG`() {
        assertEquals(WeatherCondition.FOG, WeatherConditionMapper.toDomain(48))
    }

    // ---- Drizzle ----

    @Test
    fun `code 51 maps to DRIZZLE`() {
        assertEquals(WeatherCondition.DRIZZLE, WeatherConditionMapper.toDomain(51))
    }

    @Test
    fun `code 53 maps to DRIZZLE`() {
        assertEquals(WeatherCondition.DRIZZLE, WeatherConditionMapper.toDomain(53))
    }

    @Test
    fun `code 55 maps to DRIZZLE`() {
        assertEquals(WeatherCondition.DRIZZLE, WeatherConditionMapper.toDomain(55))
    }

    @Test
    fun `code 56 maps to DRIZZLE`() {
        assertEquals(WeatherCondition.DRIZZLE, WeatherConditionMapper.toDomain(56))
    }

    @Test
    fun `code 57 maps to DRIZZLE`() {
        assertEquals(WeatherCondition.DRIZZLE, WeatherConditionMapper.toDomain(57))
    }

    // ---- Rain ----

    @Test
    fun `code 61 maps to RAIN`() {
        assertEquals(WeatherCondition.RAIN, WeatherConditionMapper.toDomain(61))
    }

    @Test
    fun `code 63 maps to RAIN`() {
        assertEquals(WeatherCondition.RAIN, WeatherConditionMapper.toDomain(63))
    }

    @Test
    fun `code 65 maps to RAIN`() {
        assertEquals(WeatherCondition.RAIN, WeatherConditionMapper.toDomain(65))
    }

    @Test
    fun `code 66 maps to RAIN`() {
        assertEquals(WeatherCondition.RAIN, WeatherConditionMapper.toDomain(66))
    }

    @Test
    fun `code 67 maps to RAIN`() {
        assertEquals(WeatherCondition.RAIN, WeatherConditionMapper.toDomain(67))
    }

    @Test
    fun `code 80 maps to RAIN`() {
        assertEquals(WeatherCondition.RAIN, WeatherConditionMapper.toDomain(80))
    }

    @Test
    fun `code 81 maps to RAIN`() {
        assertEquals(WeatherCondition.RAIN, WeatherConditionMapper.toDomain(81))
    }

    @Test
    fun `code 82 maps to RAIN`() {
        assertEquals(WeatherCondition.RAIN, WeatherConditionMapper.toDomain(82))
    }

    // ---- Snowfall ----

    @Test
    fun `code 71 maps to SNOWFALL`() {
        assertEquals(WeatherCondition.SNOWFALL, WeatherConditionMapper.toDomain(71))
    }

    @Test
    fun `code 73 maps to SNOWFALL`() {
        assertEquals(WeatherCondition.SNOWFALL, WeatherConditionMapper.toDomain(73))
    }

    @Test
    fun `code 75 maps to SNOWFALL`() {
        assertEquals(WeatherCondition.SNOWFALL, WeatherConditionMapper.toDomain(75))
    }

    @Test
    fun `code 77 maps to SNOWFALL`() {
        assertEquals(WeatherCondition.SNOWFALL, WeatherConditionMapper.toDomain(77))
    }

    @Test
    fun `code 85 maps to SNOWFALL`() {
        assertEquals(WeatherCondition.SNOWFALL, WeatherConditionMapper.toDomain(85))
    }

    @Test
    fun `code 86 maps to SNOWFALL`() {
        assertEquals(WeatherCondition.SNOWFALL, WeatherConditionMapper.toDomain(86))
    }

    // ---- Thunderstorm ----

    @Test
    fun `code 95 maps to THUNDERSTORM`() {
        assertEquals(WeatherCondition.THUNDERSTORM, WeatherConditionMapper.toDomain(95))
    }

    @Test
    fun `code 96 maps to THUNDERSTORM`() {
        assertEquals(WeatherCondition.THUNDERSTORM, WeatherConditionMapper.toDomain(96))
    }

    @Test
    fun `code 99 maps to THUNDERSTORM`() {
        assertEquals(WeatherCondition.THUNDERSTORM, WeatherConditionMapper.toDomain(99))
    }

    // ---- Unknown / fallback ----

    @Test
    fun `unknown code returns CLEAR_SKY as fallback`() {
        assertEquals(WeatherCondition.CLEAR_SKY, WeatherConditionMapper.toDomain(999))
    }

    @Test
    fun `negative code returns CLEAR_SKY as fallback`() {
        assertEquals(WeatherCondition.CLEAR_SKY, WeatherConditionMapper.toDomain(-1))
    }

    @Test
    fun `boundary code 4 (not defined) returns CLEAR_SKY`() {
        assertEquals(WeatherCondition.CLEAR_SKY, WeatherConditionMapper.toDomain(4))
    }
}
