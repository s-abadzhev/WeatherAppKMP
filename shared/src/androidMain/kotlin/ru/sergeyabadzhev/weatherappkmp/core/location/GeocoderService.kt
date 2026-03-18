package ru.sergeyabadzhev.weatherappkmp.core.location

import android.content.Context
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume

actual class GeocoderService(private val context: Context) {

    private val geocoder = Geocoder(context, Locale.getDefault())

    @Throws(LocationError::class, CancellationException::class)
    actual suspend fun reverseGeocode(lat: Double, lon: Double): City {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { cont ->
                    geocoder.getFromLocation(lat, lon, 1, Geocoder.GeocodeListener { addresses ->
                        val address = addresses.firstOrNull()
                        cont.resume(City(
                            name = address?.locality ?: "My Location",
                            country = address?.countryName ?: "",
                            latitude = lat,
                            longitude = lon
                        ))
                    })
                }
            } else {
                withContext(Dispatchers.IO) {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(lat, lon, 1)
                    val address = addresses?.firstOrNull()
                    City(
                        name = address?.locality ?: "My Location",
                        country = address?.countryName ?: "",
                        latitude = lat,
                        longitude = lon
                    )
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            City(name = "My Location", country = "", latitude = lat, longitude = lon)
        }
    }

    @Throws(LocationError::class, CancellationException::class)
    actual suspend fun searchCity(query: String): List<City> {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { cont ->
                    geocoder.getFromLocationName(query, 10, Geocoder.GeocodeListener { addresses ->
                        val cities = addresses
                            .filter { it.locality != null }
                            .map { address ->
                                City(
                                    name = address.locality,
                                    country = address.countryName ?: "",
                                    latitude = address.latitude,
                                    longitude = address.longitude
                                )
                            }
                        cont.resume(cities)
                    })
                }
            } else {
                withContext(Dispatchers.IO) {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocationName(query, 10)
                    addresses
                        ?.filter { it.locality != null }
                        ?.map { address ->
                            City(
                                name = address.locality,
                                country = address.countryName ?: "",
                                latitude = address.latitude,
                                longitude = address.longitude
                            )
                        } ?: emptyList()
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emptyList()
        }
    }
}
