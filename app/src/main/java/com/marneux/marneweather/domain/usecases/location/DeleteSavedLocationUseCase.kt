package com.marneux.marneweather.domain.usecases.location

import com.marneux.marneweather.domain.repositories.location.CurrentLocationRepository
import com.marneux.marneweather.model.weather.BriefWeatherDetails

class DeleteSavedLocationUseCase(
    private val locationRepository: CurrentLocationRepository
) {
    suspend fun execute(
        briefWeatherDetails: BriefWeatherDetails
    ) {
        return locationRepository.deleteSavedLocation(
            briefWeatherDetails
        )
    }
}