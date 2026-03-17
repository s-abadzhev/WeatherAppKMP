//
//  DetailCard.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI
import Shared

struct DetailCard: View {

    let icon: String
    let title: String
    let value: String

    var body: some View {
        GlassCard {
            VStack(alignment: .leading, spacing: 8) {
                Label(title, systemImage: icon)
                    .font(.system(size: 13, weight: .medium))
                    .foregroundStyle(.white.opacity(0.6))
                    .textCase(.uppercase)

                Text(value)
                    .font(.system(size: 28, weight: .medium))
                    .foregroundStyle(.white)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(16)
        }
    }
}
