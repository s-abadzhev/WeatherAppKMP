package ru.sergeyabadzhev.weatherappkmp.features.search

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import ru.sergeyabadzhev.weatherappkmp.fakes.FakeCityRepository
import ru.sergeyabadzhev.weatherappkmp.fakes.FakeCityStorage
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var cityRepository: FakeCityRepository
    private lateinit var cityStorage: FakeCityStorage

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        cityRepository = FakeCityRepository()
        cityStorage = FakeCityStorage()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = SearchViewModel(
        cityRepository = cityRepository,
        cityStorage = cityStorage
    )

    // ---- Initial state ----

    @Test
    fun `initial state has empty query`() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()
        assertEquals("", vm.state.value.query)
    }

    @Test
    fun `initial state has empty results`() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()
        assertTrue(vm.state.value.results.isEmpty())
    }

    @Test
    fun `initial state has no error`() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()
        assertNull(vm.state.value.error)
    }

    @Test
    fun `initial state is not loading`() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()
        assertFalse(vm.state.value.isLoading)
    }

    // ---- savedCities loaded from storage ----

    @Test
    fun `savedCities are loaded from cityStorage on init`() = runTest(testDispatcher) {
        val city = City(id = "c1", name = "Berlin", country = "Germany", latitude = 52.52, longitude = 13.40)
        cityStorage = FakeCityStorage(initial = listOf(city))
        val vm = createViewModel()

        advanceUntilIdle()

        assertEquals(1, vm.state.value.savedCities.size)
        assertEquals("Berlin", vm.state.value.savedCities[0].name)
    }

    @Test
    fun `savedCities update reactively when storage changes`() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val city = City(id = "c2", name = "Paris", country = "France", latitude = 48.85, longitude = 2.35)
        cityStorage.saveCity(city)
        advanceUntilIdle()

        assertEquals(1, vm.state.value.savedCities.size)
        assertEquals("Paris", vm.state.value.savedCities[0].name)
    }

    // ---- onQueryChanged ----

    @Test
    fun `onQueryChanged updates query in state`() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        vm.onQueryChanged("Mo")
        advanceUntilIdle()

        assertEquals("Mo", vm.state.value.query)
    }

    @Test
    fun `onQueryChanged with single char clears results and does not search`() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        vm.onQueryChanged("M")
        advanceUntilIdle()

        assertTrue(vm.state.value.results.isEmpty())
        assertEquals(0, cityRepository.searchCallCount)
    }

    @Test
    fun `onQueryChanged with empty string clears results`() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        vm.onQueryChanged("")
        advanceUntilIdle()

        assertTrue(vm.state.value.results.isEmpty())
        assertEquals(0, cityRepository.searchCallCount)
    }

    @Test
    fun `onQueryChanged with 2 chars triggers search after debounce`() = runTest(testDispatcher) {
        val results = listOf(City(id = "c1", name = "Moscow", country = "Russia", latitude = 55.75, longitude = 37.62))
        cityRepository.searchResult = Result.success(results)
        val vm = createViewModel()
        advanceUntilIdle()

        vm.onQueryChanged("Mo")
        advanceTimeBy(500) // past the 400ms debounce
        advanceUntilIdle()

        assertEquals(1, cityRepository.searchCallCount)
        assertEquals("Mo", cityRepository.lastSearchQuery)
        assertEquals(1, vm.state.value.results.size)
        assertEquals("Moscow", vm.state.value.results[0].name)
    }

    @Test
    fun `onQueryChanged does not search before debounce delay`() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        vm.onQueryChanged("Moscow")
        advanceTimeBy(300) // before the 400ms debounce

        assertEquals(0, cityRepository.searchCallCount)
    }

    @Test
    fun `rapid onQueryChanged calls only trigger one search (debounce cancels previous)`() = runTest(testDispatcher) {
        cityRepository.searchResult = Result.success(emptyList())
        val vm = createViewModel()
        advanceUntilIdle()

        vm.onQueryChanged("M")
        advanceTimeBy(100)
        vm.onQueryChanged("Mo")
        advanceTimeBy(100)
        vm.onQueryChanged("Mos")
        advanceTimeBy(100)
        vm.onQueryChanged("Mosc")
        advanceTimeBy(500) // trigger search for last query
        advanceUntilIdle()

        // Only one search should have been made, for the last query
        assertEquals(1, cityRepository.searchCallCount)
        assertEquals("Mosc", cityRepository.lastSearchQuery)
    }

    @Test
    fun `search results are cleared when query becomes too short`() = runTest(testDispatcher) {
        val results = listOf(City(id = "c1", name = "Moscow", country = "Russia", latitude = 55.75, longitude = 37.62))
        cityRepository.searchResult = Result.success(results)
        val vm = createViewModel()
        advanceUntilIdle()

        // First search to populate results
        vm.onQueryChanged("Moscow")
        advanceTimeBy(500)
        advanceUntilIdle()
        assertEquals(1, vm.state.value.results.size)

        // Now type single char - should clear results
        vm.onQueryChanged("M")
        advanceUntilIdle()

        assertTrue(vm.state.value.results.isEmpty())
    }

    @Test
    fun `search error is stored in state`() = runTest(testDispatcher) {
        cityRepository.searchResult = Result.failure(RuntimeException("Network error"))
        val vm = createViewModel()
        advanceUntilIdle()

        vm.onQueryChanged("Moscow")
        advanceTimeBy(500)
        advanceUntilIdle()

        assertNotNull(vm.state.value.error)
        assertTrue(vm.state.value.results.isEmpty())
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `successful search clears error`() = runTest(testDispatcher) {
        // First search fails
        cityRepository.searchResult = Result.failure(RuntimeException("Network error"))
        val vm = createViewModel()
        advanceUntilIdle()

        vm.onQueryChanged("Moscow")
        advanceTimeBy(500)
        advanceUntilIdle()
        assertNotNull(vm.state.value.error)

        // Second search succeeds
        cityRepository.searchResult = Result.success(
            listOf(City(id = "c1", name = "Moscow", country = "Russia", latitude = 55.75, longitude = 37.62))
        )
        vm.onQueryChanged("Mosco")
        advanceTimeBy(500)
        advanceUntilIdle()

        assertNull(vm.state.value.error)
        assertFalse(vm.state.value.isLoading)
    }

    // ---- saveCity ----

    @Test
    fun `saveCity calls cityStorage saveCity`() = runTest(testDispatcher) {
        val city = City(id = "c1", name = "Moscow", country = "Russia", latitude = 55.75, longitude = 37.62)
        val vm = createViewModel()
        advanceUntilIdle()

        vm.saveCity(city)
        advanceUntilIdle()

        assertEquals(1, cityStorage.saveCityCallCount)
        assertEquals(city, cityStorage.lastSavedCity)
    }

    @Test
    fun `saveCity adds city to savedCities`() = runTest(testDispatcher) {
        val city = City(id = "c1", name = "Moscow", country = "Russia", latitude = 55.75, longitude = 37.62)
        val vm = createViewModel()
        advanceUntilIdle()

        vm.saveCity(city)
        advanceUntilIdle()

        assertTrue(vm.state.value.savedCities.any { it.id == city.id })
    }

    // ---- removeCity ----

    @Test
    fun `removeCity calls cityStorage removeCity`() = runTest(testDispatcher) {
        val city = City(id = "c1", name = "Moscow", country = "Russia", latitude = 55.75, longitude = 37.62)
        cityStorage = FakeCityStorage(initial = listOf(city))
        val vm = createViewModel()
        advanceUntilIdle()

        vm.removeCity(city)
        advanceUntilIdle()

        assertEquals(1, cityStorage.removeCityCallCount)
        assertEquals(city, cityStorage.lastRemovedCity)
    }

    @Test
    fun `removeCity removes city from savedCities`() = runTest(testDispatcher) {
        val city = City(id = "c1", name = "Moscow", country = "Russia", latitude = 55.75, longitude = 37.62)
        cityStorage = FakeCityStorage(initial = listOf(city))
        val vm = createViewModel()
        advanceUntilIdle()

        vm.removeCity(city)
        advanceUntilIdle()

        assertTrue(vm.state.value.savedCities.none { it.id == city.id })
    }
}
