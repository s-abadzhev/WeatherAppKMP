package ru.sergeyabadzhev.weatherappkmp.domain.model

enum class WeatherCondition {
    CLEAR_SKY,
    PARTLY_CLOUDY,
    OVERCAST,
    FOG,
    DRIZZLE,
    RAIN,
    SNOWFALL,
    THUNDERSTORM;

    val title: String
        get() = when (this) {
            CLEAR_SKY -> "Clear Sky"
            PARTLY_CLOUDY -> "Partly Cloudy"
            OVERCAST -> "Overcast"
            FOG -> "Fog"
            DRIZZLE -> "Drizzle"
            RAIN -> "Rain"
            SNOWFALL -> "Snowfall"
            THUNDERSTORM -> "Thunderstorm"
        }

    val iconName: String
        get() = when (this) {
            CLEAR_SKY -> "sun_max"
            PARTLY_CLOUDY -> "cloud_sun"
            OVERCAST -> "cloud"
            FOG -> "cloud_fog"
            DRIZZLE -> "cloud_drizzle"
            RAIN -> "cloud_rain"
            SNOWFALL -> "cloud_snow"
            THUNDERSTORM -> "cloud_bolt_rain"
        }
}