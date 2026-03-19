package ru.sergeyabadzhev.weatherappkmp.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sergeyabadzhev.weatherappkmp.core.storage.CityStorageInterface
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import ru.sergeyabadzhev.weatherappkmp.domain.repository.CityRepository

data class SearchState(
    val query: String = "",
    val results: List<City> = emptyList(),
    val savedCities: List<City> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SearchViewModel(
    private val cityRepository: CityRepository,
    private val cityStorage: CityStorageInterface
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state

    private var searchJob: Job? = null

    fun subscribeToState(onState: (SearchState) -> Unit): Job =
        viewModelScope.launch { state.collect { onState(it) } }

    init {
        viewModelScope.launch {
            cityStorage.savedCities.collect { cities ->
                _state.update { it.copy(savedCities = cities) }
            }
        }
    }

    fun onQueryChanged(query: String) {
        _state.update { it.copy(query = query) }
        searchJob?.cancel()

        if (query.trim().length < 2) {
            _state.update { it.copy(results = emptyList()) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(400)
            search(query.trim())
        }
    }

    private suspend fun search(query: String) {
        _state.update { it.copy(isLoading = true, error = null) }
        try {
            val results = cityRepository.searchCity(query)
            _state.update { it.copy(results = results, isLoading = false) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message, isLoading = false, results = emptyList()) }
        }
    }

    fun saveCity(city: City) {
        viewModelScope.launch {
            cityStorage.saveCity(city)
        }
    }

    fun removeCity(city: City) {
        viewModelScope.launch {
            cityStorage.removeCity(city)
        }
    }
}
