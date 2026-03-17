//
//  SearchViewModel.swift
//  WeatherAppiOS
//
//  Created by Sergey Abadzhev on 13.03.26.
//

import Foundation
import Shared

@MainActor
@Observable
final class SearchViewModel
{

    var query: String = ""
    var results: [City] = []
    var savedCities: [City] = []
    var isLoading = false
    var error: String?

    private let cityRepository: CityRepository  // KMP протокол
    private let storage: CityStorage
    private var searchTask: Task<Void, Never>?

    init(cityRepository: CityRepository, storage: CityStorage) {
        self.cityRepository = cityRepository
        self.storage = storage
        self.savedCities = storage.loadCities()
    }

    func onQueryChanged() {
        searchTask?.cancel()

        let trimmed = query.trimmingCharacters(in: .whitespaces)
        guard trimmed.count >= 2 else {
            results = []
            return
        }

        searchTask = Task {
            try? await Task.sleep(nanoseconds: 400_000_000)
            guard !Task.isCancelled else { return }
            await search(query: trimmed)
        }
    }

    private func search(query: String) async {
        isLoading = true
        error = nil
        defer { isLoading = false }

        do {
            results = try await cityRepository.searchCity(query: query)
        } catch {
            self.error = error.localizedDescription
            results = []
        }
    }

    func saveCity(_ city: City) {
        guard !savedCities.contains(where: { $0.id == city.id }) else { return }
        savedCities.append(city)
        storage.saveCities(savedCities)
    }

    func removeCity(_ city: City) {
        savedCities.removeAll { $0.id == city.id }
        storage.saveCities(savedCities)
    }
}
