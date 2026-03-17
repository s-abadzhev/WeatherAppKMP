//
//  SearchView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI
import Shared

struct SearchView: View {

    @State var viewModel: SearchViewModel
    @Environment(\.dismiss) private var dismiss
    let onCitySelected: (City) -> Void

    var body: some View {
        ZStack {
            BackgroundView(condition: nil)

            VStack(spacing: 0) {
                SearchBarView(query: $viewModel.query) {
                    dismiss()
                }

                if viewModel.isLoading {
                    ProgressView()
                        .tint(.white)
                        .padding(.top, 40)
                } else if !viewModel.results.isEmpty {
                    SearchResultsListView(results: viewModel.results) { city in
                        viewModel.saveCity(city)
                        onCitySelected(city)
                        dismiss()
                    }
                } else if viewModel.query.count >= 2 {
                    SearchEmptyStateView(state: .noResults)
                } else if viewModel.savedCities.isEmpty {
                    SearchEmptyStateView(state: .startTyping)
                } else {
                    SavedCitiesListView(
                        cities: viewModel.savedCities,
                        onSelect: { city in
                            onCitySelected(city)
                            dismiss()
                        },
                        onDelete: { city in
                            viewModel.removeCity(city)
                        }
                    )
                }

                Spacer()
            }
        }
        .navigationBarHidden(true)
        .onChange(of: viewModel.query) {
            viewModel.onQueryChanged()
        }
    }
}
