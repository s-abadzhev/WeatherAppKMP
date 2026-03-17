//
//  LoadingView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI

struct LoadingView: View {
    var body: some View {
        VStack(spacing: 16) {
            ProgressView()
                .tint(.white)
                .scaleEffect(1.5)
            Text(L10n.Loading.weather)
                .font(.system(size: 16))
                .foregroundStyle(.white.opacity(0.7))
        }
    }
}
