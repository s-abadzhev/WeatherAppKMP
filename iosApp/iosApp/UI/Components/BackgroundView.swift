//
//  BackgroundView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI
import Shared

struct BackgroundView: View {

    let condition: WeatherCondition?

    var body: some View {
        LinearGradient(
            colors: gradientColors,
            startPoint: .topLeading,
            endPoint: .bottomTrailing
        )
        .ignoresSafeArea()
    }

    private var gradientColors: [Color] {
        switch condition {
        case .clearSky:
            return [Color(.clearSkyStart), Color(.clearSkyEnd)]
        case .partlyCloudy:
            return [Color(.partlyCloudStart), Color(.partlyCloudEnd)]
        case .overcast, .fog:
            return [Color(.overcastStart), Color(.overcastEnd)]
        case .drizzle, .rain:
            return [Color(.rainStart), Color(.rainEnd)]
        case .snowfall:
            return [Color(.snowfallStart), Color(.snowfallEnd)]
        case .thunderstorm:
            return [Color(.thunderstormStart), Color(.thunderstormEnd)]
        default:
            return [Color(.defaultStart), Color(.defaultEnd)]
        }
    }
}
