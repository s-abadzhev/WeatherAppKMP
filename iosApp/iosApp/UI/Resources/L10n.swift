//
//  L10n.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 14.03.26.
//

import Foundation
import Shared

enum L10n {

    enum Weather {
        static let clearSky = "weather.condition.clear_sky".localized
        static let partlyCloudy = "weather.condition.partly_cloudy".localized
        static let overcast = "weather.condition.overcast".localized
        static let fog = "weather.condition.fog".localized
        static let drizzle = "weather.condition.drizzle".localized
        static let rain = "weather.condition.rain".localized
        static let snowfall = "weather.condition.snowfall".localized
        static let thunderstorm = "weather.condition.thunderstorm".localized
    }

    enum Forecast {
        static let hourly = "forecast.hourly".localized
        static let daily = "forecast.daily".localized
        static let today = "forecast.today".localized
        static let tomorrow = "forecast.tomorrow".localized
    }

    enum Details {
        static let humidity = "details.humidity".localized
        static let wind = "details.wind".localized
        static let uvIndex = "details.uv_index".localized
        static let visibility = "details.visibility".localized
        static let feelsLike = "details.feels_like".localized
    }

    enum UV {
        static let low = "uv.low".localized
        static let moderate = "uv.moderate".localized
        static let high = "uv.high".localized
        static let veryHigh = "uv.very_high".localized
        static let extreme = "uv.extreme".localized
    }

    enum Search {
        static let placeholder = "search.placeholder".localized
        static let cancel = "search.cancel".localized
        static let savedCities = "search.saved_cities".localized
        static let notFound = "search.not_found".localized
        static let startTyping = "search.start_typing".localized
    }

    enum Loading {
        static let weather = "loading.weather".localized
    }

    enum Error {
        static let retry = "error.retry".localized
        static let invalidURL = "error.invalid_url".localized
        static let noInternet = "error.no_internet".localized
        static let timeout = "error.timeout".localized
        static let decoding = "error.decoding".localized
        static func server(_ code: Int) -> String {
            "error.server".localized(code)
        }
        static func windSpeed(_ value: String) -> String {
            "error.wind_speed".localized(value)
        }
    }

    enum Location {
        static let myLocation = "location.my_location".localized
        static let unknown = "location.unknown".localized
        static let permissionDenied = "location.permission_denied".localized
        static let permissionRestricted = "location.permission_restricted".localized
        static let unavailable = "location.unavailable".localized
        static let timeout = "location.timeout".localized
    }
    
}

extension WeatherCondition {
    var localizedTitle: String {
        switch self {
        case .clearSky:     return L10n.Weather.clearSky
        case .partlyCloudy: return L10n.Weather.partlyCloudy
        case .overcast:     return L10n.Weather.overcast
        case .fog:          return L10n.Weather.fog
        case .drizzle:      return L10n.Weather.drizzle
        case .rain:         return L10n.Weather.rain
        case .snowfall:     return L10n.Weather.snowfall
        case .thunderstorm: return L10n.Weather.thunderstorm
        default:            return ""
        }
    }

    var systemImage: String {
        switch self {
        case .clearSky:     return "sun.max.fill"
        case .partlyCloudy: return "cloud.sun.fill"
        case .overcast:     return "cloud.fill"
        case .fog:          return "cloud.fog.fill"
        case .drizzle:      return "cloud.drizzle.fill"
        case .rain:         return "cloud.rain.fill"
        case .snowfall:     return "cloud.snow.fill"
        case .thunderstorm: return "cloud.bolt.rain.fill"
        default:            return "sun.max.fill"
        }
    }
}
