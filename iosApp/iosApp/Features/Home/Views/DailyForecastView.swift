//
//  DailyForecastView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI
import Shared

struct DailyForecastView: View {

    let forecast: [Forecast]

    var body: some View {
        GlassCard {
            VStack(alignment: .leading, spacing: 12) {
                Label(L10n.Forecast.daily, systemImage: "calendar")
                    .font(.system(size: 13, weight: .medium))
                    .foregroundStyle(.white.opacity(0.6))
                    .textCase(.uppercase)

                Divider()
                    .background(.white.opacity(0.2))

                VStack(spacing: 0) {
                    ForEach(Array(forecast.enumerated()), id: \.offset) { index, item in
                        DailyItemView(item: item)

                        if index < forecast.count - 1 {
                            Divider()
                                .background(.white.opacity(0.1))
                        }
                    }
                }
            }
            .padding(16)
        }
    }
}
