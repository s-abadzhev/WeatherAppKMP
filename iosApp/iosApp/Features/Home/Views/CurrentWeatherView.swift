//
//  CurrentWeatherView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI
import Shared

struct CurrentWeatherView: View {

    let weather: Weather

    var body: some View {
        VStack(spacing: 8) {
            Text(weather.city.name)
                .font(.system(size: 32, weight: .medium))
                .foregroundStyle(.white)

            Text(weather.temperature.formattedTemp)
                .font(.system(size: 80, weight: .thin))
                .foregroundStyle(.white)

            Text(weather.condition.localizedTitle)
                .font(.system(size: 20, weight: .regular))
                .foregroundStyle(.white.opacity(0.8))

            HStack(spacing: 8) {
                Text("details.feels_like".localized(weather.feelsLike.formattedTemp))
                    .font(.system(size: 16))
                    .foregroundStyle(.white.opacity(0.7))
            }
        }
        .padding(.top, 16)
    }
}
