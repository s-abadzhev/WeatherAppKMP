//
//  WeatherDetailsView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI
import Shared

struct WeatherDetailsView: View {

    let weather: Weather

    var body: some View {
        LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: 12) {
            DetailCard(icon: "humidity.fill",  title: L10n.Details.humidity,    value: "\(weather.humidity)%")
            DetailCard(icon: "wind",           title: L10n.Details.wind,         value: weather.windSpeed.formattedWindSpeed)
            DetailCard(icon: "sun.max.fill",   title: L10n.Details.uvIndex,      value: weather.uvIndex.formattedUV)
            DetailCard(icon: "eye.fill",       title: L10n.Details.visibility,   value: weather.visibility.formattedVisibility)
        }
    }
}
