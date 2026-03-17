package ru.sergeyabadzhev.weatherappkmp.core.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLPlacemark
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import kotlin.coroutines.resume

actual class GeocoderService {

    private val geocoder = CLGeocoder()

    @OptIn(ExperimentalCoroutinesApi::class)
    actual suspend fun reverseGeocode(lat: Double, lon: Double): City {
        return suspendCancellableCoroutine { continuation ->
            val location = CLLocation(latitude = lat, longitude = lon)

            geocoder.reverseGeocodeLocation(location) { placemarks, error ->
                if (error != null) {
                    continuation.resume(placeholder(lat, lon))
                    return@reverseGeocodeLocation
                }
                val placemark = placemarks?.firstOrNull() as? CLPlacemark
                continuation.resume(
                    City(
                        name = placemark?.locality ?: "My Location",
                        country = placemark?.country ?: "",
                        latitude = lat,
                        longitude = lon
                    )
                )
            }

            continuation.invokeOnCancellation {
                geocoder.cancelGeocode()
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun searchCity(query: String): List<City> {
        return suspendCancellableCoroutine { continuation ->
            geocoder.geocodeAddressString(query) { placemarks, error ->
                if (error != null) {
                    continuation.resume(emptyList())
                    return@geocodeAddressString
                }
                val cities = placemarks
                    ?.filterIsInstance<CLPlacemark>()
                    ?.mapNotNull { placemark ->
                        val name = placemark.locality ?: return@mapNotNull null
                        val location = placemark.location ?: return@mapNotNull null
                        City(
                            name = name,
                            country = placemark.country ?: "",
                            latitude = location.coordinate.useContents { latitude },
                            longitude = location.coordinate.useContents { longitude }
                        )
                    } ?: emptyList()
                continuation.resume(cities)
            }

            continuation.invokeOnCancellation {
                geocoder.cancelGeocode()
            }
        }
    }

    private fun placeholder(lat: Double, lon: Double) = City(
        name = "My Location",
        country = "",
        latitude = lat,
        longitude = lon
    )
}