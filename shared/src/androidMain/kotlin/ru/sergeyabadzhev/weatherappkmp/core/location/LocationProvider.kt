package ru.sergeyabadzhev.weatherappkmp.core.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


actual class LocationProvider(private val context: Context) : LocationProviderInterface {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @Throws(LocationError::class, CancellationException::class)
    @SuppressLint("MissingPermission")
    actual override suspend fun getCurrentLocation(): Coordinates {
        try {
            return withTimeout(10_000) {
                val lastLocation = getLastLocation()
                if (lastLocation != null) return@withTimeout lastLocation
                getCurrentLocationFromGps()
            }
        } catch (e: TimeoutCancellationException) {
            throw LocationError.Timeout()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocation(): Coordinates? {
        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(Coordinates(location.latitude, location.longitude))
                    } else {
                        continuation.resume(null)
                    }
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocationFromGps(): Coordinates {
        return suspendCancellableCoroutine { continuation ->
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient
                .getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, cancellationTokenSource.token)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(Coordinates(location.latitude, location.longitude))
                    } else {
                        continuation.resumeWithException(LocationError.Unavailable())
                    }
                }
                .addOnFailureListener {
                    continuation.resumeWithException(LocationError.Unavailable())
                }

            continuation.invokeOnCancellation {
                cancellationTokenSource.cancel()
            }
        }
    }
}