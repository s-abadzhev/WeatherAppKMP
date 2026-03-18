package ru.sergeyabadzhev.weatherappkmp.di

import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.onClose
import org.koin.plugin.module.dsl.single
import org.koin.plugin.module.dsl.viewModel
import ru.sergeyabadzhev.weatherappkmp.core.network.NetworkClient
import ru.sergeyabadzhev.weatherappkmp.data.repository.CityRepositoryImpl
import ru.sergeyabadzhev.weatherappkmp.data.repository.WeatherRepositoryImpl
import ru.sergeyabadzhev.weatherappkmp.domain.repository.CityRepository
import ru.sergeyabadzhev.weatherappkmp.domain.repository.WeatherRepository
import ru.sergeyabadzhev.weatherappkmp.features.home.HomeViewModel
import ru.sergeyabadzhev.weatherappkmp.features.search.SearchViewModel

val sharedModule = module {
    single<NetworkClient>() onClose { it?.close() }
    single<CityRepositoryImpl>() bind CityRepository::class
    single<WeatherRepositoryImpl>() bind WeatherRepository::class

    viewModel<HomeViewModel>()
    viewModel<SearchViewModel>()
}
