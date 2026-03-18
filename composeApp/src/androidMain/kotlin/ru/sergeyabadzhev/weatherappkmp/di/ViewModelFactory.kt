package ru.sergeyabadzhev.weatherappkmp.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sergeyabadzhev.weatherappkmp.core.location.GeocoderService
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationProvider
import ru.sergeyabadzhev.weatherappkmp.core.network.NetworkClient
import ru.sergeyabadzhev.weatherappkmp.core.storage.CityStorage
import ru.sergeyabadzhev.weatherappkmp.core.storage.LocationPreferences
import ru.sergeyabadzhev.weatherappkmp.data.repository.CityRepositoryImpl
import ru.sergeyabadzhev.weatherappkmp.data.repository.WeatherRepositoryImpl
import ru.sergeyabadzhev.weatherappkmp.features.home.HomeViewModel
import ru.sergeyabadzhev.weatherappkmp.features.search.SearchViewModel

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val networkClient = NetworkClient()
        val geocoderService = GeocoderService(context)
        val cityRepository = CityRepositoryImpl(geocoderService)
        val weatherRepository = WeatherRepositoryImpl(networkClient, cityRepository)
        val locationProvider = LocationProvider(context)
        val locationPreferences = LocationPreferences(context)
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(weatherRepository, locationProvider, locationPreferences) as T
    }
}

class SearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val geocoderService = GeocoderService(context)
        val cityRepository = CityRepositoryImpl(geocoderService)
        val cityStorage = CityStorage(context)
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel(cityRepository, cityStorage) as T
    }
}
