package ru.sergeyabadzhev.weatherappkmp.ui.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.sergeyabadzhev.weatherappkmp.R
import ru.sergeyabadzhev.weatherappkmp.domain.model.WeatherCondition
import ru.sergeyabadzhev.weatherappkmp.features.home.HomeError

@Composable
fun WeatherCondition.localizedTitle(): String = stringResource(
    when (this) {
        WeatherCondition.CLEAR_SKY -> R.string.condition_clear_sky
        WeatherCondition.PARTLY_CLOUDY -> R.string.condition_partly_cloudy
        WeatherCondition.OVERCAST -> R.string.condition_overcast
        WeatherCondition.FOG -> R.string.condition_fog
        WeatherCondition.DRIZZLE -> R.string.condition_drizzle
        WeatherCondition.RAIN -> R.string.condition_rain
        WeatherCondition.SNOWFALL -> R.string.condition_snowfall
        WeatherCondition.THUNDERSTORM -> R.string.condition_thunderstorm
    }
)

@Composable
fun Double.localizedUV(): String = stringResource(
    when {
        this < 3 -> R.string.uv_low
        this < 6 -> R.string.uv_moderate
        this < 8 -> R.string.uv_high
        this < 11 -> R.string.uv_very_high
        else -> R.string.uv_extreme
    }
)

@Composable
fun Double.localizedWindSpeed(): String =
    stringResource(R.string.wind_speed, this.toBigDecimal().toPlainString())

@Composable
fun Double.localizedVisibility(): String =
    if (this >= 1000) "${(this / 1000).toInt()} km" else "${this.toInt()} m"

@Composable
fun HomeError.localizedMessage(): String = stringResource(
    when (this) {
        HomeError.PermissionDenied -> R.string.error_permission_denied
        HomeError.LocationUnavailable -> R.string.error_unavailable
        HomeError.LocationTimeout -> R.string.error_timeout
        HomeError.NetworkError -> R.string.error_unavailable
    }
)
