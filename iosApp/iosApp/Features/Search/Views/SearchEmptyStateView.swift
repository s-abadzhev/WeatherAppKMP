//
//  SearchEmptyStateView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 14.03.26.
//

import SwiftUI

enum SearchEmptyState {
    case noResults
    case startTyping
}

struct SearchEmptyStateView: View {

    let state: SearchEmptyState

    var body: some View {
        VStack(spacing: 12) {
            Spacer().frame(height: 40)

            Image(systemName: iconName)
                .font(.system(size: 36))
                .foregroundStyle(.white.opacity(0.4))

            Text(message)
                .font(.system(size: 16))
                .foregroundStyle(.white.opacity(0.5))
        }
    }

    private var iconName: String {
        switch state {
        case .noResults: return "magnifyingglass"
        case .startTyping: return "mappin.slash"
        }
    }

    private var message: String {
        switch state {
        case .noResults: return L10n.Search.notFound
        case .startTyping: return L10n.Search.startTyping
        }
    }
}
