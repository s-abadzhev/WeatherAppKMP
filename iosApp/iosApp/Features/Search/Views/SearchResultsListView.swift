//
//  SearchResultsListView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 14.03.26.
//

import SwiftUI
import Shared

struct SearchResultsListView: View {

    let results: [City]
    let onSelect: (City) -> Void

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 0) {
                ForEach(results) { city in
                    CityRowView(city: city) {
                        onSelect(city)
                    }
                }
            }
            .padding(.horizontal, 16)
        }
    }
}
