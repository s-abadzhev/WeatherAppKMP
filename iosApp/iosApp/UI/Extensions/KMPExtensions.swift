//
//  KMPExtensions.swift
//  iosApp
//
//  Created by Sergey Abadzhev on 17.03.26.
//

import Foundation
import Shared

extension HourlyForecast: @retroactive Identifiable {
    public var id: String {
        date.epochSeconds.description
    }
}

extension Forecast: @retroactive Identifiable {
    public var id: String {
        "\(date.year)-\(date.month.ordinal)-\(date.day)"
    }
}

extension City: @retroactive Identifiable {}
