package ru.sergeyabadzhev.weatherappkmp.features.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.sergeyabadzhev.weatherappkmp.R
import ru.sergeyabadzhev.weatherappkmp.domain.model.Forecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.HourlyForecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.Weather
import ru.sergeyabadzhev.weatherappkmp.domain.model.WeatherCondition
import ru.sergeyabadzhev.weatherappkmp.ui.components.GlassCard
import ru.sergeyabadzhev.weatherappkmp.ui.extensions.emoji
import ru.sergeyabadzhev.weatherappkmp.ui.extensions.formattedHour
import ru.sergeyabadzhev.weatherappkmp.ui.extensions.formattedUV
import ru.sergeyabadzhev.weatherappkmp.ui.extensions.formattedVisibility
import ru.sergeyabadzhev.weatherappkmp.ui.extensions.formattedWeekday
import ru.sergeyabadzhev.weatherappkmp.ui.extensions.localizedWeekday
import ru.sergeyabadzhev.weatherappkmp.ui.resources.localizedMessage
import ru.sergeyabadzhev.weatherappkmp.ui.resources.localizedTitle
import ru.sergeyabadzhev.weatherappkmp.ui.resources.localizedUV
import ru.sergeyabadzhev.weatherappkmp.ui.resources.localizedVisibility
import ru.sergeyabadzhev.weatherappkmp.ui.resources.localizedWindSpeed


@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (granted) {
            viewModel.onLocationPermissionGranted()
        } else {
            viewModel.onPermissionDenied()
        }
    }

    // Запускаем onAppear один раз
    LaunchedEffect(Unit) {
        viewModel.onAppear()
    }

    // Реагируем на запрос геолокации
    LaunchedEffect(state.needsLocationUpdate) {
        if (state.needsLocationUpdate) {
            val granted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

            if (granted) {
                viewModel.onLocationPermissionGranted()
            } else {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient(state.weather?.condition))
    ) {
        when {
            state.isLoading -> LoadingView()
            state.error != null -> ErrorView(
                message = state.error!!.localizedMessage(),
                onRetry = {
                    if (state.isUsingDeviceLocation) {
                        viewModel.switchToDeviceLocation()
                    } else {
                        viewModel.onAppear()
                    }
                }
            )
            state.weather != null -> WeatherContentView(
                weather = state.weather!!,
                hourlyForecast = state.hourlyForecast,
                dailyForecast = state.dailyForecast
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!state.isUsingDeviceLocation) {
                TextButton(onClick = { viewModel.switchToDeviceLocation() }) {
                    Text(
                        text = "📍",
                        fontSize = 24.sp
                    )
                }
            }
            TextButton(onClick = onSearchClick) {
                Text(
                    text = "🔍",
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
private fun WeatherContentView(
    weather: Weather,
    hourlyForecast: List<HourlyForecast>,
    dailyForecast: List<Forecast>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 60.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { CurrentWeatherView(weather = weather) }
        item { HourlyForecastView(forecast = hourlyForecast) }
        item { DailyForecastView(forecast = dailyForecast) }
        item { WeatherDetailsView(weather = weather) }
    }
}

@Composable
private fun CurrentWeatherView(weather: Weather) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weather.city.name,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        Text(
            text = "${weather.temperature.toInt()}°",
            fontSize = 80.sp,
            fontWeight = FontWeight.Thin,
            color = Color.White
        )
        Text(
            text = weather.condition.localizedTitle(),
            fontSize = 20.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
        Text(
            text = stringResource(R.string.details_feels_like, "${weather.feelsLike.toInt()}°"),
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun HourlyForecastView(forecast: List<HourlyForecast>) {
    GlassCard {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionLabel(text = stringResource(R.string.forecast_hourly), icon = "🕐")
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                items(forecast) { item ->
                    HourlyItemView(item = item)
                }
            }
        }
    }
}

@Composable
private fun HourlyItemView(item: HourlyForecast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = item.date.formattedHour(),
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = item.condition.emoji,
            fontSize = 22.sp
        )
        Text(
            text = "${item.temperature.toInt()}°",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        if (item.precipitationProbability > 20) {
            Text(
                text = "${item.precipitationProbability}%",
                fontSize = 12.sp,
                color = Color(0xFF4FC3F7)
            )
        }
    }
}

@Composable
private fun DailyForecastView(forecast: List<Forecast>) {
    GlassCard {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionLabel(text = stringResource(R.string.forecast_daily), icon = "📅")
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            forecast.forEachIndexed { index, item ->
                DailyItemView(item = item)
                if (index < forecast.size - 1) {
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                }
            }
        }
    }
}

@Composable
private fun DailyItemView(item: Forecast) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.date.localizedWeekday(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.width(100.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        if (item.precipitationProbability > 20) {
            Text(
                text = "${item.precipitationProbability}%",
                fontSize = 13.sp,
                color = Color(0xFF4FC3F7),
                modifier = Modifier.width(36.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(36.dp))
        }
        Text(
            text = item.condition.emoji,
            fontSize = 20.sp,
            modifier = Modifier.width(32.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${item.tempMin.toInt()}°",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${item.tempMax.toInt()}°",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
private fun WeatherDetailsView(weather: Weather) {
    val items = listOf(
        Triple("💧", stringResource(R.string.details_humidity), "${weather.humidity}%"),
        Triple("💨", stringResource(R.string.details_wind), weather.windSpeed.localizedWindSpeed()),
        Triple("☀️", stringResource(R.string.details_uv_index), weather.uvIndex.localizedUV()),
        Triple("👁️", stringResource(R.string.details_visibility), weather.visibility.localizedVisibility())
    )

    androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
        modifier = Modifier.height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items.size) { index ->
            val (icon, title, value) = items[index]
            DetailCard(icon = icon, title = title, value = value)
        }
    }
}

@Composable
private fun DetailCard(icon: String, title: String, value: String) {
    GlassCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "$icon $title",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Composable
private fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.loading_weather), color = Color.White.copy(alpha = 0.7f))
        }
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text("⚠️", fontSize = 48.sp)
            Text(
                text = message,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            Button(onClick = onRetry) {
                Text(stringResource(R.string.error_retry))
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String, icon: String) {
    Text(
        text = "$icon $text".uppercase(),
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White.copy(alpha = 0.6f)
    )
}

private fun backgroundGradient(condition: WeatherCondition?): Brush {
    val colors = when (condition) {
        WeatherCondition.CLEAR_SKY -> listOf(Color(0xFF1a6bcc), Color(0xFF0d3b8c))
        WeatherCondition.PARTLY_CLOUDY -> listOf(Color(0xFF3a7bd5), Color(0xFF1e4d9b))
        WeatherCondition.OVERCAST, WeatherCondition.FOG -> listOf(Color(0xFF546e7a), Color(0xFF2c3e50))
        WeatherCondition.DRIZZLE, WeatherCondition.RAIN -> listOf(Color(0xFF2c3e50), Color(0xFF1a252f))
        WeatherCondition.SNOWFALL -> listOf(Color(0xFF7ba7c9), Color(0xFF4a6fa5))
        WeatherCondition.THUNDERSTORM -> listOf(Color(0xFF1c1c2e), Color(0xFF0d0d1a))
        null -> listOf(Color(0xFF1a6bcc), Color(0xFF0d3b8c))
    }
    return Brush.linearGradient(colors)
}