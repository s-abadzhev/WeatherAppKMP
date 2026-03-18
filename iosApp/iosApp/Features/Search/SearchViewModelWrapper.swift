//
//  SearchViewModelWrapper.swift
//  iosApp
//
//  Created by Sergey Abadzhev on 18.03.26.
//

import Foundation
import Observation
import Shared

@MainActor
@Observable
final class SearchViewModelWrapper {

    private(set) var state: SearchState
    private let vm: SearchViewModel
    private var job: Kotlinx_coroutines_coreJob?

    var query: String = ""

    var results: [City] { state.results }
    var savedCities: [City] { state.savedCities }
    var isLoading: Bool { state.isLoading }

    init(vm: SearchViewModel) {
        self.vm = vm
        self.state = vm.state.value as! SearchState
        self.query = state.query
        job = vm.subscribeToState { [weak self] newState in
            guard let self else { return }
            self.state = newState
            if self.query != newState.query {
                self.query = newState.query
            }
        }
    }

    func onQueryChanged() {
        vm.onQueryChanged(query: query)
    }

    func saveCity(_ city: City) {
        vm.saveCity(city: city)
    }

    func removeCity(_ city: City) {
        vm.removeCity(city: city)
    }
}
