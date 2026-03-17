//
//  CityStorageDto.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import Foundation
import Shared

struct CityStorageDTO: Codable {
    let id: String
    let name: String
    let country: String
    let latitude: Double
    let longitude: Double

    init(from city: City) {
        self.id = city.id
        self.name = city.name
        self.country = city.country
        self.latitude = city.latitude
        self.longitude = city.longitude
    }

    func toDomain() -> City {
        City(
            id: id,
            name: name,
            country: country,
            latitude: latitude,
            longitude: longitude
        )
    }
}
