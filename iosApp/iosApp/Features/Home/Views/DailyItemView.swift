//
//  DailyItemView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI
import Shared

struct DailyItemView: View {

    let item: Forecast

    var body: some View {
        HStack {
            Text(item.date.formattedWeekday)
                .font(.system(size: 16, weight: .medium))
                .foregroundStyle(.white)
                .frame(width: 110, alignment: .leading)

            Spacer()

            if item.precipitationProbability > 20 {
                Text("\(item.precipitationProbability)%")
                    .font(.system(size: 13))
                    .foregroundStyle(.cyan.opacity(0.9))
                    .frame(width: 36)
            } else {
                Spacer().frame(width: 36)
            }

            Image(systemName: item.condition.systemImage)
                .font(.system(size: 20))
                .foregroundStyle(.white)
                .frame(width: 32)

            Spacer()

            HStack(spacing: 8) {
                Text(item.tempMin.formattedTemp)
                    .font(.system(size: 16))
                    .foregroundStyle(.white.opacity(0.5))

                Text(item.tempMax.formattedTemp)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundStyle(.white)
            }
        }
        .padding(.vertical, 10)
    }
}
