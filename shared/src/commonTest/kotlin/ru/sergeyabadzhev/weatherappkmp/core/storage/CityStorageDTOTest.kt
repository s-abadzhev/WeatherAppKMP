package ru.sergeyabadzhev.weatherappkmp.core.storage

import ru.sergeyabadzhev.weatherappkmp.domain.model.City
import kotlin.test.Test
import kotlin.test.assertEquals

class CityStorageDTOTest {

    private val testCity = City(
        id = "fixed-id-123",
        name = "London",
        country = "UK",
        latitude = 51.5074,
        longitude = -0.1278
    )

    private val testDto = CityStorageDTO(
        id = "fixed-id-123",
        name = "London",
        country = "UK",
        latitude = 51.5074,
        longitude = -0.1278
    )

    // ---- toDomain ----

    @Test
    fun `toDomain maps id correctly`() {
        assertEquals(testDto.id, testDto.toDomain().id)
    }

    @Test
    fun `toDomain maps name correctly`() {
        assertEquals("London", testDto.toDomain().name)
    }

    @Test
    fun `toDomain maps country correctly`() {
        assertEquals("UK", testDto.toDomain().country)
    }

    @Test
    fun `toDomain maps latitude correctly`() {
        assertEquals(51.5074, testDto.toDomain().latitude)
    }

    @Test
    fun `toDomain maps longitude correctly`() {
        assertEquals(-0.1278, testDto.toDomain().longitude)
    }

    // ---- fromDomain ----

    @Test
    fun `fromDomain maps id correctly`() {
        assertEquals(testCity.id, CityStorageDTO.fromDomain(testCity).id)
    }

    @Test
    fun `fromDomain maps name correctly`() {
        assertEquals("London", CityStorageDTO.fromDomain(testCity).name)
    }

    @Test
    fun `fromDomain maps country correctly`() {
        assertEquals("UK", CityStorageDTO.fromDomain(testCity).country)
    }

    @Test
    fun `fromDomain maps latitude correctly`() {
        assertEquals(51.5074, CityStorageDTO.fromDomain(testCity).latitude)
    }

    @Test
    fun `fromDomain maps longitude correctly`() {
        assertEquals(-0.1278, CityStorageDTO.fromDomain(testCity).longitude)
    }

    // ---- Round-trip ----

    @Test
    fun `City - DTO - City round-trip preserves all fields`() {
        val dto = CityStorageDTO.fromDomain(testCity)
        val result = dto.toDomain()
        assertEquals(testCity.id, result.id)
        assertEquals(testCity.name, result.name)
        assertEquals(testCity.country, result.country)
        assertEquals(testCity.latitude, result.latitude)
        assertEquals(testCity.longitude, result.longitude)
    }

    @Test
    fun `DTO - City - DTO round-trip preserves all fields`() {
        val city = testDto.toDomain()
        val result = CityStorageDTO.fromDomain(city)
        assertEquals(testDto.id, result.id)
        assertEquals(testDto.name, result.name)
        assertEquals(testDto.country, result.country)
        assertEquals(testDto.latitude, result.latitude)
        assertEquals(testDto.longitude, result.longitude)
    }
}
