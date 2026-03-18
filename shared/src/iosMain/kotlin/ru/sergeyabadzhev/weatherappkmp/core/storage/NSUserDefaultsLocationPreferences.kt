package ru.sergeyabadzhev.weatherappkmp.core.storage

import platform.Foundation.NSUserDefaults

class NSUserDefaultsLocationPreferences : LocationPreferencesInterface {

    private companion object {
        const val KEY_USE_DEVICE = "use_device_location"
        const val KEY_LAT = "saved_lat"
        const val KEY_LON = "saved_lon"
        const val KEY_CITY_NAME = "saved_city_name"
    }

    override suspend fun saveDeviceLocation() {
        NSUserDefaults.standardUserDefaults.setBool(true, KEY_USE_DEVICE)
    }

    override suspend fun saveSelectedCity(lat: Double, lon: Double, cityName: String) {
        val defaults = NSUserDefaults.standardUserDefaults
        defaults.setBool(false, KEY_USE_DEVICE)
        defaults.setDouble(lat, KEY_LAT)
        defaults.setDouble(lon, KEY_LON)
        defaults.setObject(cityName, KEY_CITY_NAME)
    }

    override suspend fun getLastLocation(): LastLocation {
        val defaults = NSUserDefaults.standardUserDefaults
        val useDevice = defaults.boolForKey(KEY_USE_DEVICE).let {
            if (!defaults.objectForKey(KEY_USE_DEVICE).let { obj -> obj != null }) true else it
        }
        if (useDevice) return LastLocation.DeviceLocation

        val lat = defaults.doubleForKey(KEY_LAT)
        val lon = defaults.doubleForKey(KEY_LON)
        val name = defaults.stringForKey(KEY_CITY_NAME)

        return if (name != null) {
            LastLocation.SelectedCity(lat, lon, name)
        } else {
            LastLocation.DeviceLocation
        }
    }
}
