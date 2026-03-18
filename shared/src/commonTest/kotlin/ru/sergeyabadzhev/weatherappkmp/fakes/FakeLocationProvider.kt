package ru.sergeyabadzhev.weatherappkmp.fakes

import ru.sergeyabadzhev.weatherappkmp.core.location.Coordinates
import ru.sergeyabadzhev.weatherappkmp.core.location.LocationProviderInterface

class FakeLocationProvider : LocationProviderInterface {

    var result: Result<Coordinates> = Result.success(Coordinates(55.75, 37.62))
    var callCount = 0

    override suspend fun getCurrentLocation(): Coordinates {
        callCount++
        return result.getOrThrow()
    }
}
