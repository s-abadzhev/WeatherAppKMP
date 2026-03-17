package ru.sergeyabadzhev.weatherappkmp.core.location

import android.content.Context
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import java.util.Locale

actual class GeocoderService(private val context: Context) {

    private val geocoder = Geocoder(context, Locale.getDefault())

    actual suspend fun reverseGeocode(lat: Double, lon: Double): City {
        return withContext(Dispatchers.IO) {
            try {
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                val address = addresses?.firstOrNull()
                City(
                    name = address?.locality ?: "My Location",
                    country = address?.countryName ?: "",
                    latitude = lat,
                    longitude = lon
                )
            } catch (e: Exception) {
                City(
                    name = "My Location",
                    country = "",
                    latitude = lat,
                    longitude = lon
                )
            }
        }
    }

    actual suspend fun searchCity(query: String): List<City> {
        return withContext(Dispatchers.IO) {
            try {
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
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}