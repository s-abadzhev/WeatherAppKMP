//
//  ErrorView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI

struct ErrorView: View {

    let message: String
    let onRetry: () -> Void

    var body: some View {
        VStack(spacing: 20) {
            Image(systemName: "exclamationmark.triangle.fill")
                .font(.system(size: 48))
                .foregroundStyle(.white.opacity(0.8))

            Text(message)
                .font(.system(size: 16))
                .foregroundStyle(.white.opacity(0.7))
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)

            Button(action: onRetry) {
                Text(L10n.Error.retry)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundStyle(.white)
                    .padding(.horizontal, 32)
                    .padding(.vertical, 12)
                    .background(.ultraThinMaterial)
                    .clipShape(Capsule())
            }
        }
    }
}
