package ru.sergeyabadzhev.weatherappkmp.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.sergeyabadzhev.weatherappkmp.core.location.GeocoderService
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationProvider
import ru.sergeyabadzhev.weatherappkmp.core.storage.CityStorage
import ru.sergeyabadzhev.weatherappkmp.core.storage.CityStorageInterface
import ru.sergeyabadzhev.weatherappkmp.core.storage.LocationPreferences
import ru.sergeyabadzhev.weatherappkmp.core.storage.LocationPreferencesInterface
import ru.sergeyabadzhev.weatherappkmp.features.home.HomeViewModel
import ru.sergeyabadzhev.weatherappkmp.features.search.SearchViewModel

val androidModule = module {
    single { GeocoderService(androidContext()) }
    single { LocationProvider(androidContext()) }
    single<LocationPreferencesInterface> { LocationPreferences(androidContext()) }
    single<CityStorageInterface> { CityStorage(androidContext()) }

    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
}
