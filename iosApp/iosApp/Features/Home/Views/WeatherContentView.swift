//
//  WeatherContentView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI
import Shared

struct WeatherContentView: View {

    let weather: Weather
    let hourlyForecast: [HourlyForecast]
    let dailyForecast: [Forecast]

    var body: some View {
        ScrollView(showsIndicators: false) {
            VStack(spacing: 24) {
                CurrentWeatherView(weather: weather)
                HourlyForecastView(forecast: hourlyForecast)
                DailyForecastView(forecast: dailyForecast)
                WeatherDetailsView(weather: weather)
            }
            .padding(16)
        }
    }
}
