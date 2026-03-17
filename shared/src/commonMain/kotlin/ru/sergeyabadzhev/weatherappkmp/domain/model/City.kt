package ru.sergeyabadzhev.weatherappkmp.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class City @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)