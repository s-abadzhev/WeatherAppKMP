//
//  GlassCard.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import SwiftUI

struct GlassCard<Content: View>: View {

    let content: () -> Content

    init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }

    var body: some View {
        content()
            .background(.ultraThinMaterial.opacity(0.6))
            .clipShape(RoundedRectangle(cornerRadius: 16))
            .overlay {
                RoundedRectangle(cornerRadius: 16)
                    .stroke(.white.opacity(0.15), lineWidth: 1)
            }
    }
}
