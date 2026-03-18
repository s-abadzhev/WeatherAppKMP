package ru.sergeyabadzhev.weatherappkmp.di

import org.koin.dsl.module
import ru.sergeyabadzhev.weatherappkmp.core.network.NetworkClient
import ru.sergeyabadzhev.weatherappkmp.data.repository.CityRepositoryImpl
import ru.sergeyabadzhev.weatherappkmp.data.repository.WeatherRepositoryImpl
import ru.sergeyabadzhev.weatherappkmp.domain.repository.CityRepository
import ru.sergeyabadzhev.weatherappkmp.domain.repository.WeatherRepository

val sharedModule = module {
    single { NetworkClient() }
    single<CityRepository> { CityRepositoryImpl(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
}
