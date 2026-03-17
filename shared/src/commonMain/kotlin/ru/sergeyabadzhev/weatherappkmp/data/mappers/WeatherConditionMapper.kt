package ru.sergeyabadzhev.weatherappkmp.data.mappers

import ru.sergeyabadzhev.weatherappkmp.domain.model.WeatherCondition

object WeatherConditionMapper {

    fun toDomain(code: Int): WeatherCondition = when (code) {
        0 -> WeatherCondition.CLEAR_SKY
        1, 2 -> WeatherCondition.PARTLY_CLOUDY
        3 -> WeatherCondition.OVERCAST
        45, 48 -> WeatherCondition.FOG
        51, 53, 55, 56, 57 -> WeatherCondition.DRIZZLE
        61, 63, 65, 66, 67, 80, 81, 82 -> WeatherCondition.RAIN
        71, 73, 75, 77, 85, 86 -> WeatherCondition.SNOWFALL
        95, 96, 99 -> WeatherCondition.THUNDERSTORM
        else -> WeatherCondition.CLEAR_SKY
    }
}