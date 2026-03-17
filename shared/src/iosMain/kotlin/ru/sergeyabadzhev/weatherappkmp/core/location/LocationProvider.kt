package ru.sergeyabadzhev.weatherappkmp.core.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyKilometer
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class LocationProvider {

    @OptIn(ExperimentalForeignApi::class)
    @Throws(LocationError::class, CancellationException::class)
    actual suspend fun getCurrentLocation(): Coordinates {
        return suspendCancellableCoroutine { continuation ->
            val delegate = LocationDelegate(
                onLocation = { location ->
                    continuation.resume(
                        Coordinates(
                            latitude = location.coordinate.useContents { latitude },
                            longitude = location.coordinate.useContents { longitude }
                        )
                    )
                },
                onError = { error ->
                    continuation.resumeWithException(error)
                }
            )

            val manager = CLLocationManager()
            manager.delegate = delegate
            manager.desiredAccuracy = kCLLocationAccuracyKilometer
            manager.requestWhenInUseAuthorization()
            manager.requestLocation()

            continuation.invokeOnCancellation {
                manager.stopUpdatingLocation()
            }
        }
    }
}

private class LocationDelegate(
    private val onLocation: (CLLocation) -> Unit,
    private val onError: (LocationError) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {

    override fun locationManager(
        manager: CLLocationManager,
        didUpdateLocations: List<*>
    ) {
        val location = didUpdateLocations.lastOrNull() as? CLLocation ?: return
        onLocation(location)
    }

    override fun locationManager(
        manager: CLLocationManager,
        didFailWithError: NSError
    ) {
        onError(LocationError.Unavailable())
    }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        when (manager.authorizationStatus) {
            kCLAuthorizationStatusDenied -> onError(LocationError.PermissionDenied())
            kCLAuthorizationStatusRestricted -> onError(LocationError.PermissionRestricted())
            else -> {}
        }
    }
}