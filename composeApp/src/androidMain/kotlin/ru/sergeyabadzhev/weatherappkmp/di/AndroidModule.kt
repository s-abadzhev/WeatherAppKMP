package ru.sergeyabadzhev.weatherappkmp.di

import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single
import ru.sergeyabadzhev.weatherappkmp.core.location.GeocoderService
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationProvider
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationProviderInterface
import ru.sergeyabadzhev.weatherappkmp.core.storage.CityStorage
import ru.sergeyabadzhev.weatherappkmp.core.storage.CityStorageInterface
import ru.sergeyabadzhev.weatherappkmp.core.storage.LocationPreferences
import ru.sergeyabadzhev.weatherappkmp.core.storage.LocationPreferencesInterface

val androidModule = module {
    single<GeocoderService>()
    single<LocationProvider>() bind LocationProviderInterface::class
    single<LocationPreferences>() bind LocationPreferencesInterface::class
    single<CityStorage>() bind CityStorageInterface::class
}
