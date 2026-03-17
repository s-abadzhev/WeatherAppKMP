//
//  HourlyItemView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI
import Shared

struct HourlyItemView: View {

    let item: HourlyForecast

    var body: some View {
        VStack(spacing: 8) {
            Text(item.date.formattedHour)
                .font(.system(size: 14))
                .foregroundStyle(.white.opacity(0.7))

            Image(systemName: item.condition.systemImage)
                .font(.system(size: 22))
                .foregroundStyle(.white)
                .frame(height: 28)

            Text(item.temperature.formattedTemp)
                .font(.system(size: 16, weight: .medium))
                .foregroundStyle(.white)

            if item.precipitationProbability > 20 {
                Text("\(item.precipitationProbability)%")
                    .font(.system(size: 12))
                    .foregroundStyle(.cyan.opacity(0.9))
            } else {
                Spacer()
                    .frame(height: 15)
            }
        }
    }
}
