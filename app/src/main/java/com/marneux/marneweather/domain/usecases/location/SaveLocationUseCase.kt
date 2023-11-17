package com.marneux.marneweather.domain.usecases.location

import com.marneux.marneweather.domain.repositories.location.CurrentLocationRepository

class SaveLocationUseCase(
    private val locationRepository: CurrentLocationRepository
) {
    suspend fun execute(
        nameLocation: String,
        latitude: String,
        longitude: String
    ) {
        return locationRepository.saveWeatherLocation(
            nameLocation,
            latitude,
            longitude
        )
    }
}



