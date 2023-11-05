package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.cajondesastre.location.models.location.SavedLocation
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetSavedLocationsListStreamUseCase(
    private val weatherRepository: WeatherRepository
) {
    fun execute(): Flow<List<SavedLocation>> {
        return weatherRepository.getSavedLocationsListStream()
    }

}
