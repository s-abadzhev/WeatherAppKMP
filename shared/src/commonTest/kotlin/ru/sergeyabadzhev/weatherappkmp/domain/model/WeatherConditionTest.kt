package ru.sergeyabadzhev.weatherappkmp.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WeatherConditionTest {

    // ---- title property ----

    @Test
    fun `CLEAR_SKY title is correct`() {
        assertEquals("Clear Sky", WeatherCondition.CLEAR_SKY.title)
    }

    @Test
    fun `PARTLY_CLOUDY title is correct`() {
        assertEquals("Partly Cloudy", WeatherCondition.PARTLY_CLOUDY.title)
    }

    @Test
    fun `OVERCAST title is correct`() {
        assertEquals("Overcast", WeatherCondition.OVERCAST.title)
    }

    @Test
    fun `FOG title is correct`() {
        assertEquals("Fog", WeatherCondition.FOG.title)
    }

    @Test
    fun `DRIZZLE title is correct`() {
        assertEquals("Drizzle", WeatherCondition.DRIZZLE.title)
    }

    @Test
    fun `RAIN title is correct`() {
        assertEquals("Rain", WeatherCondition.RAIN.title)
    }

    @Test
    fun `SNOWFALL title is correct`() {
        assertEquals("Snowfall", WeatherCondition.SNOWFALL.title)
    }

    @Test
    fun `THUNDERSTORM title is correct`() {
        assertEquals("Thunderstorm", WeatherCondition.THUNDERSTORM.title)
    }

    // ---- iconName property ----

    @Test
    fun `CLEAR_SKY iconName is correct`() {
        assertEquals("sun_max", WeatherCondition.CLEAR_SKY.iconName)
    }

    @Test
    fun `PARTLY_CLOUDY iconName is correct`() {
        assertEquals("cloud_sun", WeatherCondition.PARTLY_CLOUDY.iconName)
    }

    @Test
    fun `OVERCAST iconName is correct`() {
        assertEquals("cloud", WeatherCondition.OVERCAST.iconName)
    }

    @Test
    fun `FOG iconName is correct`() {
        assertEquals("cloud_fog", WeatherCondition.FOG.iconName)
    }

    @Test
    fun `DRIZZLE iconName is correct`() {
        assertEquals("cloud_drizzle", WeatherCondition.DRIZZLE.iconName)
    }

    @Test
    fun `RAIN iconName is correct`() {
        assertEquals("cloud_rain", WeatherCondition.RAIN.iconName)
    }

    @Test
    fun `SNOWFALL iconName is correct`() {
        assertEquals("cloud_snow", WeatherCondition.SNOWFALL.iconName)
    }

    @Test
    fun `THUNDERSTORM iconName is correct`() {
        assertEquals("cloud_bolt_rain", WeatherCondition.THUNDERSTORM.iconName)
    }

    // ---- Completeness ----

    @Test
    fun `all conditions have non-blank title`() {
        WeatherCondition.entries.forEach { condition ->
            assertTrue(condition.title.isNotBlank(), "title for $condition is blank")
        }
    }

    @Test
    fun `all conditions have non-blank iconName`() {
        WeatherCondition.entries.forEach { condition ->
            assertTrue(condition.iconName.isNotBlank(), "iconName for $condition is blank")
        }
    }

    @Test
    fun `all conditions have unique titles`() {
        val titles = WeatherCondition.entries.map { it.title }
        assertEquals(titles.size, titles.toSet().size, "Duplicate titles found")
    }

    @Test
    fun `all conditions have unique iconNames`() {
        val icons = WeatherCondition.entries.map { it.iconName }
        assertEquals(icons.size, icons.toSet().size, "Duplicate iconNames found")
    }
}
