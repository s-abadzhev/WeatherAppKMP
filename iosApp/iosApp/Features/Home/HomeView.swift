//
//  HomeView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 18.03.26.
//

import SwiftUI
import Shared

struct HomeView: View {

    @State var viewModel: HomeViewModelWrapper
    let selectedCity: City?

    @Environment(AppCoordinator.self) private var coordinator

    var body: some View {
        ZStack {
            BackgroundView(condition: viewModel.weather?.condition)

            Group {
                if viewModel.isLoading {
                    LoadingView()
                } else if let error = viewModel.error {
                    ErrorView(message: error) {
                        viewModel.loadWeatherForCurrentLocation()
                    }
                } else if let weather = viewModel.weather {
                    WeatherContentView(
                        weather: weather,
                        hourlyForecast: viewModel.hourlyForecast,
                        dailyForecast: viewModel.dailyForecast
                    )
                }
            }

            VStack {
                HStack {
                    Spacer()
                    HStack(spacing: 4) {
                        if !viewModel.isUsingDeviceLocation {
                            Button {
                                viewModel.switchToDeviceLocation()
                            } label: {
                                Image(systemName: "location.fill")
                                    .font(.system(size: 18, weight: .medium))
                                    .foregroundStyle(.white)
                                    .padding(12)
                                    .background(.ultraThinMaterial.opacity(0.6))
                                    .clipShape(Circle())
                            }
                        }

                        Button {
                            coordinator.isSearchPresented = true
                        } label: {
                            Image(systemName: "magnifyingglass")
                                .font(.system(size: 18, weight: .medium))
                                .foregroundStyle(.white)
                                .padding(12)
                                .background(.ultraThinMaterial.opacity(0.6))
                                .clipShape(Circle())
                        }
                    }
                    .padding(.trailing, 16)
                }
                Spacer()
            }
        }
        .task {
            viewModel.onAppear()
        }
        .onChange(of: selectedCity) { _, new in
            guard let new else { return }
            viewModel.loadWeatherForCity(new)
        }
    }
}
