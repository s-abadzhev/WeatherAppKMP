//
//  Double+Formatting.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import Foundation

extension Double {

    var formattedTemp: String {
        let rounded = Int(rounded())
        return "\(rounded)°"
    }

    var formattedVisibility: String {
        if self >= 1000 {
            return "\(Int(self / 1000)) км"
        }
        return "\(Int(self)) м"
    }

    var formattedUV: String {
        switch self {
        case 0..<3:  return L10n.UV.low
        case 3..<6:  return L10n.UV.moderate
        case 6..<8:  return L10n.UV.high
        case 8..<11: return L10n.UV.veryHigh
        default:     return L10n.UV.extreme
        }
    }

    var formattedWindSpeed: String {
        L10n.Error.windSpeed(
            formatted(.number.precision(.fractionLength(1)))
        )
    }
}
