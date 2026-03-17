package ru.sergeyabadzhev.weatherappkmp.core.location

sealed class LocationError : Exception() {
    class PermissionDenied : LocationError()
    class PermissionRestricted : LocationError()
    class Unavailable : LocationError()
    class Timeout : LocationError()
}