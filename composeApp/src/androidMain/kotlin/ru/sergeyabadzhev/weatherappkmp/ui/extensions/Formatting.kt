package ru.sergeyabadzhev.weatherappkmp.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.sergeyabadzhev.weatherappkmp.domain.model.WeatherCondition
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.sergeyabadzhev.weatherappkmp.R
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Instant

fun Instant.formattedHour(): String {
    val local = toLocalDateTime(TimeZone.currentSystemDefault())
    return "%02d:%02d".format(local.hour, local.minute)
}

val WeatherCondition.emoji: String
    get() = when (this) {
        WeatherCondition.CLEAR_SKY -> "☀️"
        WeatherCondition.PARTLY_CLOUDY -> "⛅"
        WeatherCondition.OVERCAST -> "☁️"
        WeatherCondition.FOG -> "🌫️"
        WeatherCondition.DRIZZLE -> "🌦️"
        WeatherCondition.RAIN -> "🌧️"
        WeatherCondition.SNOWFALL -> "❄️"
        WeatherCondition.THUNDERSTORM -> "⛈️"
    }

@Composable
fun LocalDate.localizedWeekday(): String {
    val javaDate = java.time.LocalDate.of(year, monthNumber, dayOfMonth)
    val today = java.time.LocalDate.now()
    return when (javaDate) {
        today -> stringResource(R.string.forecast_today)
        today.plusDays(1) -> stringResource(R.string.forecast_tomorrow)
        else -> javaDate.dayOfWeek
            .getDisplayName(TextStyle.FULL, Locale.getDefault())
            .replaceFirstChar { it.uppercase() }
    }
}