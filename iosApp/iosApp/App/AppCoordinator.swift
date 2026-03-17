//
//  AppCoordinator.swift
//  iosApp
//
//  Created by Sergey Abadzhev on 17.03.26.
//

import Foundation
import Observation
import Shared

@MainActor
@Observable
final class AppCoordinator {

    var selectedCity: City?
    var isSearchPresented = false

    let homeViewModel: HomeViewModel
    let searchViewModel: SearchViewModel

    init() {
        let networkClient = NetworkClient()
        let geocoderService = GeocoderService()
        let cityRepo = CityRepositoryImpl(geocoderService: geocoderService)
        let weatherRepo = WeatherRepositoryImpl(
            networkClient: networkClient,
            cityRepository: cityRepo
        )
        let locationManager = LocationProvider()
        let locationPreferences = LocationPreferences()
        let cityStorage = UserDefaultsCityStorage()

        self.homeViewModel = HomeViewModel(
            weatherRepository: weatherRepo,
            locationManager: locationManager,
            locationPreferences: locationPreferences
        )
        self.searchViewModel = SearchViewModel(
            cityRepository: cityRepo,
            storage: cityStorage
        )
    }
}
