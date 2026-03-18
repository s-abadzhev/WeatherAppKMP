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

    var isSearchPresented = false
    var selectedCity: City?

    let homeViewModel: HomeViewModelWrapper
    let searchViewModel: SearchViewModelWrapper

    init() {
        let networkClient = NetworkClient()
        let geocoderService = GeocoderService()
        let cityRepo = CityRepositoryImpl(geocoderService: geocoderService)
        let weatherRepo = WeatherRepositoryImpl(
            networkClient: networkClient,
            cityRepository: cityRepo
        )
        let locationProvider = LocationProvider()
        let locationPrefs = NSUserDefaultsLocationPreferences()
        let cityStorage = NSUserDefaultsCityStorage()

        let homeVM = HomeViewModel(
            weatherRepository: weatherRepo,
            locationProvider: locationProvider,
            locationPreferences: locationPrefs
        )
        let searchVM = SearchViewModel(
            cityRepository: cityRepo,
            cityStorage: cityStorage
        )

        self.homeViewModel = HomeViewModelWrapper(vm: homeVM)
        self.searchViewModel = SearchViewModelWrapper(vm: searchVM)
    }
}
