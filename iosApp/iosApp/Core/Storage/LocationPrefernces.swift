//
//  LocationPrefernces.swift
//  iosApp
//
//  Created by Sergey Abadzhev on 17.03.26.
//

import Foundation

enum LastLocation {
    case deviceLocation
    case selectedCity(lat: Double, lon: Double, name: String)
}

final class LocationPreferences {

    private let defaults: UserDefaults
    private let useDeviceLocationKey = "use_device_location"
    private let savedLatKey = "saved_lat"
    private let savedLonKey = "saved_lon"
    private let savedCityNameKey = "saved_city_name"

    init(defaults: UserDefaults = .standard) {
        self.defaults = defaults
    }

    func saveDeviceLocation() {
        defaults.set(true, forKey: useDeviceLocationKey)
    }

    func saveSelectedCity(lat: Double, lon: Double, cityName: String) {
        defaults.set(false, forKey: useDeviceLocationKey)
        defaults.set(lat, forKey: savedLatKey)
        defaults.set(lon, forKey: savedLonKey)
        defaults.set(cityName, forKey: savedCityNameKey)
    }

    func getLastLocation() -> LastLocation {
        let useDeviceLocation = defaults.object(forKey: useDeviceLocationKey) as? Bool ?? true

        if useDeviceLocation {
            return .deviceLocation
        }

        let lat = defaults.double(forKey: savedLatKey)
        let lon = defaults.double(forKey: savedLonKey)
        let name = defaults.string(forKey: savedCityNameKey)

        if let name, lat != 0, lon != 0 {
            return .selectedCity(lat: lat, lon: lon, name: name)
        }

        return .deviceLocation
    }
}
