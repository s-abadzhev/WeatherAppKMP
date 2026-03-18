package ru.sergeyabadzhev.weatherappkmp.features.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationError
import ru.sergeyabadzhev.weatherappkmp.core.storage.LastLocation
import ru.sergeyabadzhev.weatherappkmp.fakes.FakeCityStorage
import ru.sergeyabadzhev.weatherappkmp.fakes.FakeLocationPreferences
import ru.sergeyabadzhev.weatherappkmp.fakes.FakeLocationProvider
import ru.sergeyabadzhev.weatherappkmp.fakes.FakeWeatherRepository
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var weatherRepository: FakeWeatherRepository
    private lateinit var locationProvider: FakeLocationProvider
    private lateinit var locationPreferences: FakeLocationPreferences

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        weatherRepository = FakeWeatherRepository()
        locationProvider = FakeLocationProvider()
        locationPreferences = FakeLocationPreferences()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = HomeViewModel(
        weatherRepository = weatherRepository,
        locationProvider = locationProvider,
        locationPreferences = locationPreferences
    )

    // ---- Initial state ----

    @Test
    fun `initial state is correct`() = runTest(testDispatcher) {
        val vm = createViewModel()
        val state = vm.state.value
        assertNull(state.weather)
        assertTrue(state.dailyForecast.isEmpty())
        assertTrue(state.hourlyForecast.isEmpty())
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertTrue(state.isUsingDeviceLocation)
        assertFalse(state.needsLocationUpdate)
    }

    // ---- onAppear with DeviceLocation ----

    @Test
    fun `onAppear with DeviceLocation sets needsLocationUpdate true`() = runTest(testDispatcher) {
        locationPreferences = FakeLocationPreferences(LastLocation.DeviceLocation)
        val vm = createViewModel()

        vm.onAppear()
        advanceUntilIdle()

        assertTrue(vm.state.value.needsLocationUpdate)
        assertTrue(vm.state.value.isUsingDeviceLocation)
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `onAppear with DeviceLocation does not load weather`() = runTest(testDispatcher) {
        locationPreferences = FakeLocationPreferences(LastLocation.DeviceLocation)
        val vm = createViewModel()

        vm.onAppear()
        advanceUntilIdle()

        assertEquals(0, weatherRepository.fetchWeatherCallCount)
    }

    // ---- onAppear with SelectedCity ----

    @Test
    fun `onAppear with SelectedCity loads weather immediately`() = runTest(testDispatcher) {
        locationPreferences = FakeLocationPreferences(
            LastLocation.SelectedCity(lat = 48.85, lon = 2.35, name = "Paris")
        )
        val vm = createViewModel()

        vm.onAppear()
        advanceUntilIdle()

        assertNotNull(vm.state.value.weather)
        assertFalse(vm.state.value.isLoading)
        assertNull(vm.state.value.error)
    }

    @Test
    fun `onAppear with SelectedCity uses correct coordinates`() = runTest(testDispatcher) {
        locationPreferences = FakeLocationPreferences(
            LastLocation.SelectedCity(lat = 48.85, lon = 2.35, name = "Paris")
        )
        val vm = createViewModel()

        vm.onAppear()
        advanceUntilIdle()

        assertEquals(48.85, weatherRepository.lastFetchedLat)
        assertEquals(2.35, weatherRepository.lastFetchedLon)
    }

    @Test
    fun `onAppear with SelectedCity sets isUsingDeviceLocation to false`() = runTest(testDispatcher) {
        locationPreferences = FakeLocationPreferences(
            LastLocation.SelectedCity(lat = 48.85, lon = 2.35, name = "Paris")
        )
        val vm = createViewModel()

        vm.onAppear()
        advanceUntilIdle()

        assertFalse(vm.state.value.isUsingDeviceLocation)
        assertFalse(vm.state.value.needsLocationUpdate)
    }

    // ---- onPermissionDenied ----

    @Test
    fun `onPermissionDenied sets PermissionDenied error`() = runTest(testDispatcher) {
        val vm = createViewModel()

        vm.onPermissionDenied()

        val state = vm.state.value
        assertEquals(HomeError.PermissionDenied, state.error)
        assertFalse(state.isLoading)
        assertFalse(state.needsLocationUpdate)
    }

    // ---- onLocationPermissionGranted ----

    @Test
    fun `onLocationPermissionGranted fetches location and loads weather`() = runTest(testDispatcher) {
        val vm = createViewModel()

        vm.onLocationPermissionGranted()
        advanceUntilIdle()

        assertEquals(1, locationProvider.callCount)
        assertEquals(1, weatherRepository.fetchWeatherCallCount)
        assertNotNull(vm.state.value.weather)
        assertFalse(vm.state.value.isLoading)
        assertNull(vm.state.value.error)
    }

    @Test
    fun `onLocationPermissionGranted saves device location to preferences`() = runTest(testDispatcher) {
        val vm = createViewModel()

        vm.onLocationPermissionGranted()
        advanceUntilIdle()

        assertEquals(1, locationPreferences.saveDeviceLocationCallCount)
    }

    @Test
    fun `onLocationPermissionGranted with LocationError_Timeout sets timeout error`() = runTest(testDispatcher) {
        locationProvider.result = Result.failure(LocationError.Timeout())
        val vm = createViewModel()

        vm.onLocationPermissionGranted()
        advanceUntilIdle()

        assertEquals(HomeError.LocationTimeout, vm.state.value.error)
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `onLocationPermissionGranted with generic exception sets LocationUnavailable error`() = runTest(testDispatcher) {
        locationProvider.result = Result.failure(RuntimeException("GPS off"))
        val vm = createViewModel()

        vm.onLocationPermissionGranted()
        advanceUntilIdle()

        assertEquals(HomeError.LocationUnavailable, vm.state.value.error)
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `onLocationPermissionGranted sets needsLocationUpdate to false`() = runTest(testDispatcher) {
        val vm = createViewModel()

        vm.onLocationPermissionGranted()
        advanceUntilIdle()

        assertFalse(vm.state.value.needsLocationUpdate)
    }

    // ---- loadWeatherForCity ----

    @Test
    fun `loadWeatherForCity loads weather for given city`() = runTest(testDispatcher) {
        val city = City(id = "city-1", name = "Berlin", country = "Germany", latitude = 52.52, longitude = 13.40)
        val vm = createViewModel()

        vm.loadWeatherForCity(city)
        advanceUntilIdle()

        assertNotNull(vm.state.value.weather)
        assertEquals(52.52, weatherRepository.lastFetchedLat)
        assertEquals(13.40, weatherRepository.lastFetchedLon)
    }

    @Test
    fun `loadWeatherForCity saves city to preferences`() = runTest(testDispatcher) {
        val city = City(id = "city-1", name = "Berlin", country = "Germany", latitude = 52.52, longitude = 13.40)
        val vm = createViewModel()

        vm.loadWeatherForCity(city)
        advanceUntilIdle()

        assertEquals(1, locationPreferences.saveSelectedCityCallCount)
        assertEquals(52.52, locationPreferences.lastSavedLat)
        assertEquals(13.40, locationPreferences.lastSavedLon)
        assertEquals("Berlin", locationPreferences.lastSavedCityName)
    }

    @Test
    fun `loadWeatherForCity sets isUsingDeviceLocation to false`() = runTest(testDispatcher) {
        val city = City(id = "city-1", name = "Berlin", country = "Germany", latitude = 52.52, longitude = 13.40)
        val vm = createViewModel()

        vm.loadWeatherForCity(city)
        advanceUntilIdle()

        assertFalse(vm.state.value.isUsingDeviceLocation)
        assertFalse(vm.state.value.needsLocationUpdate)
    }

    @Test
    fun `loadWeatherForCity clears error`() = runTest(testDispatcher) {
        val city = City(id = "city-1", name = "Berlin", country = "Germany", latitude = 52.52, longitude = 13.40)
        val vm = createViewModel()
        vm.onPermissionDenied() // set an error first

        vm.loadWeatherForCity(city)
        advanceUntilIdle()

        assertNull(vm.state.value.error)
    }

    // ---- switchToDeviceLocation ----

    @Test
    fun `switchToDeviceLocation sets isUsingDeviceLocation true`() = runTest(testDispatcher) {
        val vm = createViewModel()

        vm.switchToDeviceLocation()
        advanceUntilIdle()

        assertTrue(vm.state.value.isUsingDeviceLocation)
    }

    @Test
    fun `switchToDeviceLocation sets needsLocationUpdate true`() = runTest(testDispatcher) {
        val vm = createViewModel()

        vm.switchToDeviceLocation()
        advanceUntilIdle()

        assertTrue(vm.state.value.needsLocationUpdate)
    }

    @Test
    fun `switchToDeviceLocation clears weather and error`() = runTest(testDispatcher) {
        val vm = createViewModel()
        // First load weather
        vm.onLocationPermissionGranted()
        advanceUntilIdle()
        assertNotNull(vm.state.value.weather)

        vm.switchToDeviceLocation()
        advanceUntilIdle()

        assertNull(vm.state.value.weather)
        assertNull(vm.state.value.error)
    }

    @Test
    fun `switchToDeviceLocation saves device location preference`() = runTest(testDispatcher) {
        val vm = createViewModel()
        locationPreferences.saveDeviceLocationCallCount = 0 // reset

        vm.switchToDeviceLocation()
        advanceUntilIdle()

        assertEquals(1, locationPreferences.saveDeviceLocationCallCount)
    }

    // ---- Network error handling ----

    @Test
    fun `network error in weather fetch sets NetworkError`() = runTest(testDispatcher) {
        weatherRepository.weatherResult = Result.failure(RuntimeException("No internet"))
        val vm = createViewModel()

        vm.onLocationPermissionGranted()
        advanceUntilIdle()

        assertEquals(HomeError.NetworkError, vm.state.value.error)
        assertFalse(vm.state.value.isLoading)
    }
}
