package com.marneux.marneweather.domain.usecases.location

import com.marneux.marneweather.domain.repositories.location.CurrentLocationRepository

class RestoreLocationUseCase(
    private val locationRepository: CurrentLocationRepository
) {
    suspend fun execute(
        nameLocation: String
    ) {
        return locationRepository.restoreDeletedLocation(
            nameLocation
        )
    }
}