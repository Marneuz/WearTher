package com.marneux.marneweather.domain.usecases.location

import com.marneux.marneweather.domain.repositories.location.GeocoderRepository

class LocationNameCoordsUseCase(
    private val geocoderRepository: GeocoderRepository
) {
    suspend fun execute(
        latitude: Double,
        longitude: Double
    ): Result<String> {
        return geocoderRepository.getLocationName(
            latitude,
            longitude
        )
    }
}