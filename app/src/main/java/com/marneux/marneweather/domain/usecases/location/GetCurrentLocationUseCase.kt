package com.marneux.marneweather.domain.usecases.location


import com.marneux.marneweather.domain.repositories.location.CurrentLocationRepository
import com.marneux.marneweather.model.location.Coordinates

class GetCurrentLocationUseCase(
    private val currentLocation: CurrentLocationRepository
) {
    suspend fun execute(): Result<Coordinates> {
        return currentLocation.getCurrentLocation()
    }
}

