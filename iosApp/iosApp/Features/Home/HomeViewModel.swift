//
//  HomeViewModel.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import Foundation
import CoreLocation
import Shared

@MainActor
@Observable
final class HomeViewModel {

    var weather: Weather?
    var dailyForecast: [Forecast] = []
    var hourlyForecast: [HourlyForecast] = []
    var isLoading = false
    var error: String?
    var isUsingDeviceLocation: Bool = true

    private let weatherRepository: WeatherRepository
    private let locationManager: LocationProvider
    private let locationPreferences: LocationPreferences
    private var locationTask: Task<Void, Never>?

    init(
        weatherRepository: WeatherRepository,
        locationManager: LocationProvider,
        locationPreferences: LocationPreferences = LocationPreferences()
    ) {
        self.weatherRepository = weatherRepository
        self.locationManager = locationManager
        self.locationPreferences = locationPreferences
    }

    func onAppear() async {
            switch locationPreferences.getLastLocation() {
            case .deviceLocation:
                isUsingDeviceLocation = true
                await loadWeatherForCurrentLocation()
            case .selectedCity(let lat, let lon, _):
                isUsingDeviceLocation = false
                await loadWeather(lat: lat, lon: lon)
            }
        }

    func loadWeatherForCurrentLocation() async {
            locationTask?.cancel()
            isUsingDeviceLocation = true
            isLoading = true
            error = nil
            defer { isLoading = false }

            locationPreferences.saveDeviceLocation()

            do {
                let location = try await locationManager.getCurrentLocation()
                await loadWeather(
                    lat: location.latitude,
                    lon: location.longitude
                )
            } catch {
                self.error = error.localizedDescription
            }
        }

    func loadWeatherForCity(_ city: City) async {
        locationTask?.cancel()
        isUsingDeviceLocation = false
        isLoading = true
        error = nil
        defer { isLoading = false }

        locationPreferences.saveSelectedCity(
            lat: city.latitude,
            lon: city.longitude,
            cityName: city.name
        )

        await loadWeather(lat: city.latitude, lon: city.longitude)
    }

    func switchToDeviceLocation() async {
        locationTask?.cancel()
        isUsingDeviceLocation = true
        weather = nil
        error = nil
        locationPreferences.saveDeviceLocation()
        await loadWeatherForCurrentLocation()
    }

    func loadWeather(lat: Double, lon: Double) async {
        error = nil
        do {
            async let weatherRequest = weatherRepository.fetchCurrentWeather(lat: lat, lon: lon)
            async let forecastRequest = weatherRepository.fetchForecast(lat: lat, lon: lon)

            let (weather, forecast) = try await (weatherRequest, forecastRequest)

            self.weather = weather
            self.dailyForecast = forecast.daily
            self.hourlyForecast = forecast.hourly
        } catch {
            self.error = error.localizedDescription
        }
    }
}
