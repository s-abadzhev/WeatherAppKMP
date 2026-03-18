//
//  HomeViewModelWrapper.swift
//  iosApp
//
//  Created by Sergey Abadzhev on 18.03.26.
//

import Foundation
import Observation
import Shared

@MainActor
@Observable
final class HomeViewModelWrapper {

    private(set) var state: HomeState
    private let vm: HomeViewModel
    private var job: Kotlinx_coroutines_coreJob?

    var weather: Weather? { state.weather }
    var dailyForecast: [Forecast] { state.dailyForecast }
    var hourlyForecast: [HourlyForecast] { state.hourlyForecast }
    var isLoading: Bool { state.isLoading }
    var isUsingDeviceLocation: Bool { state.isUsingDeviceLocation }

    var error: String? {
        guard let e = state.error else { return nil }
        switch e.name {
        case "PermissionDenied":    return L10n.Location.permissionDenied
        case "LocationUnavailable": return L10n.Location.unavailable
        case "LocationTimeout":     return L10n.Location.timeout
        default:                    return L10n.Location.unknown
        }
    }

    init(vm: HomeViewModel) {
        self.vm = vm
        self.state = vm.state.value as! HomeState
        job = vm.subscribeToState { [weak self] newState in
            guard let self else { return }
            let wasNeedsUpdate = self.state.needsLocationUpdate
            self.state = newState
            if newState.needsLocationUpdate && !wasNeedsUpdate {
                self.vm.onLocationPermissionGranted()
            }
        }
    }

    func onAppear() {
        vm.onAppear()
    }

    func loadWeatherForCurrentLocation() {
        vm.onLocationPermissionGranted()
    }

    func loadWeatherForCity(_ city: City) {
        vm.loadWeatherForCity(city: city)
    }

    func switchToDeviceLocation() {
        vm.switchToDeviceLocation()
    }

    func retry() {
        vm.retry()
    }
}
