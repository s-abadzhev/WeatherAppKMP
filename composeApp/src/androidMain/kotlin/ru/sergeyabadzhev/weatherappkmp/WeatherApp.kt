package ru.sergeyabadzhev.weatherappkmp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.sergeyabadzhev.weatherappkmp.di.androidModule
import ru.sergeyabadzhev.weatherappkmp.di.sharedModule

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApp)
            modules(sharedModule, androidModule)
        }
    }
}
