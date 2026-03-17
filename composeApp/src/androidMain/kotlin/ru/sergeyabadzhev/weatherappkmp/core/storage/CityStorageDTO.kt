package ru.sergeyabadzhev.weatherappkmp.core.storage

import ru.sergeyabadzhev.weatherappkmp.domain.model.City

@kotlinx.serialization.Serializable
data class CityStorageDTO(
    val id: String,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
) {
    fun toDomain() = City(id = id, name = name, country = country, latitude = latitude, longitude = longitude)

    companion object {
        fun fromDomain(city: City) = CityStorageDTO(
            id = city.id,
            name = city.name,
            country = city.country,
            latitude = city.latitude,
            longitude = city.longitude
        )
    }
}