//
//  CityRowView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI
import Shared

struct CityRowView: View {

    let city: City
    var isSaved: Bool = false
    let onTap: () -> Void
    var onDelete: (() -> Void)? = nil

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: 12) {
                Image(systemName: "mappin.circle.fill")
                    .font(.system(size: 22))
                    .foregroundStyle(.white.opacity(0.7))

                VStack(alignment: .leading, spacing: 2) {
                    Text(city.name)
                        .font(.system(size: 16, weight: .medium))
                        .foregroundStyle(.white)

                    Text(city.country)
                        .font(.system(size: 13))
                        .foregroundStyle(.white.opacity(0.5))
                }

                Spacer()

                if isSaved, let onDelete {
                    Button {
                        onDelete()
                    } label: {
                        Image(systemName: "trash")
                            .font(.system(size: 16))
                            .foregroundStyle(.white.opacity(0.4))
                    }
                    .buttonStyle(.plain)
                }
            }
            .padding(.vertical, 12)
        }
        .buttonStyle(.plain)

        Divider()
            .background(.white.opacity(0.1))
    }
}
