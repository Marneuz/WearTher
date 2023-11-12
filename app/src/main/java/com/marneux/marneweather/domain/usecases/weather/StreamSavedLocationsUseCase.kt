package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import com.marneux.marneweather.model.location.SavedLocation
import kotlinx.coroutines.flow.Flow

class StreamSavedLocationsUseCase(
    private val weatherRepository: WeatherRepository
) {
    fun execute(): Flow<List<SavedLocation>> {
        return weatherRepository.getSavedLocationsListStream()
    }

}
