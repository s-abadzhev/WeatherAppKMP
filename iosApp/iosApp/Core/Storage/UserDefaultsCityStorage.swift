//
//  UserDefaultsCityStorage.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import Foundation
import Shared

final class UserDefaultsCityStorage: CityStorage {

    private let key = "saved_cities"
    private let defaults: UserDefaults
    private let encoder = JSONEncoder()
    private let decoder = JSONDecoder()

    init(defaults: UserDefaults = .standard) {
        self.defaults = defaults
    }

    func saveCities(_ cities: [City]) {
        let dtos = cities.map { CityStorageDTO(from: $0) }
        if let data = try? encoder.encode(dtos) {
            defaults.set(data, forKey: key)
        }
    }

    func loadCities() -> [City] {
        guard
            let data = defaults.data(forKey: key),
            let dtos = try? decoder.decode([CityStorageDTO].self, from: data)
        else { return [] }

        return dtos.map { $0.toDomain() }
    }
}
