package ru.sergeyabadzhev.weatherappkmp.ui.resources

import android.content.Context
import ru.sergeyabadzhev.weatherappkmp.R
import ru.sergeyabadzhev.weatherappkmp.domain.model.WeatherCondition

object Strings {

    fun weatherCondition(context: Context, condition: WeatherCondition): String {
        val resId = when (condition) {
            WeatherCondition.CLEAR_SKY -> R.string.condition_clear_sky
            WeatherCondition.PARTLY_CLOUDY -> R.string.condition_partly_cloudy
            WeatherCondition.OVERCAST -> R.string.condition_overcast
            WeatherCondition.FOG -> R.string.condition_fog
            WeatherCondition.DRIZZLE -> R.string.condition_drizzle
            WeatherCondition.RAIN -> R.string.condition_rain
            WeatherCondition.SNOWFALL -> R.string.condition_snowfall
            WeatherCondition.THUNDERSTORM -> R.string.condition_thunderstorm
        }
        return context.getString(resId)
    }

    fun uvIndex(context: Context, value: Double): String {
        val resId = when {
            value < 3 -> R.string.uv_low
            value < 6 -> R.string.uv_moderate
            value < 8 -> R.string.uv_high
            value < 11 -> R.string.uv_very_high
            else -> R.string.uv_extreme
        }
        return context.getString(resId)
    }

    fun windSpeed(context: Context, value: Double): String {
        return context.getString(R.string.wind_speed, value.toString())
    }

    fun feelsLike(context: Context, value: String): String {
        return context.getString(R.string.details_feels_like, value)
    }

    fun visibility(context: Context, value: Double): String {
        return if (value >= 1000) "${(value / 1000).toInt()} km" else "${value.toInt()} m"
    }
}