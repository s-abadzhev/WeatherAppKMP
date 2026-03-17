//
//  String+Localized.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 14.03.26.
//

import Foundation

extension String {
    var localized: String {
        NSLocalizedString(self, comment: "")
    }

    func localized(_ args: CVarArg...) -> String {
        String(format: localized, args)
    }
}
