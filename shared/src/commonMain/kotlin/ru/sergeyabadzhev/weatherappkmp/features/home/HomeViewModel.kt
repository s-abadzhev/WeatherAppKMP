package ru.sergeyabadzhev.weatherappkmp.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationError
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationProviderInterface
import ru.sergeyabadzhev.weatherappkmp.core.storage.LastLocation
import ru.sergeyabadzhev.weatherappkmp.core.storage.LocationPreferencesInterface
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import ru.sergeyabadzhev.weatherappkmp.domain.model.Forecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.HourlyForecast
import ru.sergeyabadzhev.weatherappkmp.domain.model.Weather
import ru.sergeyabadzhev.weatherappkmp.domain.repository.WeatherRepository
import kotlin.coroutines.cancellation.CancellationException

enum class HomeError {
    PermissionDenied,
    LocationUnavailable,
    LocationTimeout,
    NetworkError
}

data class HomeState(
    val weather: Weather? = null,
    val dailyForecast: List<Forecast> = emptyList(),
    val hourlyForecast: List<HourlyForecast> = emptyList(),
    val isLoading: Boolean = false,
    val error: HomeError? = null,
    val errorMessage: String? = null,
    val isUsingDeviceLocation: Boolean = true,
    val needsLocationUpdate: Boolean = false
)

class HomeViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProviderInterface,
    private val locationPreferences: LocationPreferencesInterface
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    fun subscribeToState(onState: (HomeState) -> Unit): Job =
        viewModelScope.launch { state.collect { onState(it) } }

    fun onAppear() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val last = locationPreferences.getLastLocation()) {
                is LastLocation.DeviceLocation -> {
                    _state.update {
                        it.copy(
                            isUsingDeviceLocation = true,
                            isLoading = false,
                            needsLocationUpdate = true
                        )
                    }
                }
                is LastLocation.SelectedCity -> {
                    _state.update {
                        it.copy(
                            isUsingDeviceLocation = false,
                            needsLocationUpdate = false
                        )
                    }
                    loadWeather(last.lat, last.lon)
                }
            }
        }
    }

    private var locationJob: Job? = null

    fun onLocationPermissionGranted() {
        _state.update { it.copy(needsLocationUpdate = false) }

        locationJob?.cancel()
        locationJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            locationPreferences.saveDeviceLocation()
            try {
                val coordinates = locationProvider.getCurrentLocation()
                if (_state.value.isUsingDeviceLocation) {
                    loadWeather(coordinates.latitude, coordinates.longitude)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: LocationError.Timeout) {
                if (_state.value.isUsingDeviceLocation) {
                    _state.update { it.copy(isLoading = false, error = HomeError.LocationTimeout) }
                }
            } catch (e: Exception) {
                if (_state.value.isUsingDeviceLocation) {
                    _state.update { it.copy(isLoading = false, error = HomeError.LocationUnavailable) }
                }
            }
        }
    }

    fun onPermissionDenied() {
        _state.update {
            it.copy(
                isLoading = false,
                needsLocationUpdate = false,
                error = HomeError.PermissionDenied
            )
        }
    }

    fun loadWeatherForCity(city: City) {
        locationJob?.cancel()
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    isUsingDeviceLocation = false,
                    needsLocationUpdate = false
                )
            }
            locationPreferences.saveSelectedCity(city.latitude, city.longitude, city.name)
            loadWeather(city.latitude, city.longitude)
        }
    }

    fun switchToDeviceLocation() {
        locationJob?.cancel()
        _state.update {
            it.copy(
                isUsingDeviceLocation = true,
                weather = null,
                error = null,
                isLoading = false,
                needsLocationUpdate = true
            )
        }
        viewModelScope.launch {
            locationPreferences.saveDeviceLocation()
        }
    }

    private suspend fun loadWeather(lat: Double, lon: Double) {
        try {
            val weatherDeferred = viewModelScope.async { weatherRepository.fetchCurrentWeather(lat, lon) }
            val forecastDeferred = viewModelScope.async { weatherRepository.fetchForecast(lat, lon) }

            val weather = weatherDeferred.await()
            val (daily, hourly) = forecastDeferred.await()

            _state.update {
                it.copy(
                    weather = weather,
                    dailyForecast = daily,
                    hourlyForecast = hourly,
                    isLoading = false,
                    error = null
                )
            }
        } catch (e: Exception) {
            _state.update { it.copy(isLoading = false, error = HomeError.NetworkError) }
        }
    }
}
