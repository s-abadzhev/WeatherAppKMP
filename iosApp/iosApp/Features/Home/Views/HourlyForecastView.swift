//
//  HourlyForecastView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI
import Shared

struct HourlyForecastView: View {

    let forecast: [HourlyForecast]

    var body: some View {
        GlassCard {
            VStack(alignment: .leading, spacing: 12) {
                Label(L10n.Forecast.hourly, systemImage: "clock")
                    .font(.system(size: 13, weight: .medium))
                    .foregroundStyle(.white.opacity(0.6))
                    .textCase(.uppercase)

                Divider()
                    .background(.white.opacity(0.2))

                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 20) {
                        ForEach(forecast) { item in
                            HourlyItemView(item: item)
                        }
                    }
                    .padding(.horizontal, 4)
                }
            }
            .padding(16)
        }
    }
}


