//
//  SearchBarView.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 14.03.26.
//

import SwiftUI

struct SearchBarView: View {

    @Binding var query: String
    let onCancel: () -> Void

    var body: some View {
        HStack(spacing: 12) {
            HStack(spacing: 8) {
                Image(systemName: "magnifyingglass")
                    .foregroundStyle(.white.opacity(0.6))

                TextField("", text: $query, prompt:
                    Text(L10n.Search.placeholder)
                        .foregroundStyle(.white.opacity(0.4))
                )
                .foregroundStyle(.white)
                .autocorrectionDisabled()

                if !query.isEmpty {
                    Button {
                        query = ""
                    } label: {
                        Image(systemName: "xmark.circle.fill")
                            .foregroundStyle(.white.opacity(0.5))
                    }
                }
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 10)
            .background(.ultraThinMaterial.opacity(0.6))
            .clipShape(RoundedRectangle(cornerRadius: 12))

            Button(L10n.Search.cancel, action: onCancel)
                .foregroundStyle(.white)
        }
        .padding(.horizontal, 16)
        .padding(.top, 16)
        .padding(.bottom, 12)
    }
}
