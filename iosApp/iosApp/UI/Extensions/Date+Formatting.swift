//
//  Date+Formatting.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import Foundation
import Shared

extension Kotlinx_datetimeLocalDate {
    var formattedWeekday: String {
        var components = DateComponents()
        components.year = Int(self.year)
        components.month = Int(self.self.month.ordinal + 1)
        components.day = Int(self.day)

        guard let date = Calendar.current.date(from: components) else {
            return ""
        }

        if Calendar.current.isDateInToday(date) { return L10n.Forecast.today }
        if Calendar.current.isDateInTomorrow(date) { return L10n.Forecast.tomorrow }

        let formatter = DateFormatter()
        formatter.locale = Locale.current
        formatter.dateFormat = "EEEE"
        return formatter.string(from: date).capitalized
    }
}

extension KotlinInstant {
    var formattedHour: String {
        let seconds = self.epochSeconds
        let date = Date(timeIntervalSince1970: Double(seconds))

        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter.string(from: date)
    }
}
