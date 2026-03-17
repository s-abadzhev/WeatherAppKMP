//
//  SavedCitiesListView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 14.03.26.
//

import SwiftUI
import Shared

struct SavedCitiesListView: View {

    let cities: [City]
    let onSelect: (City) -> Void
    let onDelete: (City) -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(L10n.Search.savedCities)
                .font(.system(size: 13, weight: .medium))
                .foregroundStyle(.white.opacity(0.5))
                .textCase(.uppercase)
                .padding(.horizontal, 16)
                .padding(.top, 8)

            ScrollView {
                LazyVStack(spacing: 0) {
                    ForEach(cities) { city in
                        CityRowView(city: city, isSaved: true) {
                            onSelect(city)
                        } onDelete: {
                            onDelete(city)
                        }
                    }
                }
                .padding(.horizontal, 16)
            }
        }
    }
}
