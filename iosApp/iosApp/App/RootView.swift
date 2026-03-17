//
//  RootView.swift
//  iosApp
//
//  Created by Sergey Abadzhev on 17.03.26.
//

import SwiftUI

struct RootView: View {

    @Environment(AppCoordinator.self) private var coordinator

    var body: some View {
        @Bindable var coordinator = coordinator
        HomeView(
            viewModel: coordinator.homeViewModel,
            selectedCity: coordinator.selectedCity
        )
        .fullScreenCover(isPresented: $coordinator.isSearchPresented) {
            SearchView(
                viewModel: coordinator.searchViewModel
            ) { city in
                coordinator.selectedCity = city
                Task {
                    await coordinator.homeViewModel.loadWeatherForCity(city)
                }
                coordinator.isSearchPresented = false
            }
        }
    }
}
