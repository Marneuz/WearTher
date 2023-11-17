package com.marneux.marneweather.domain.usecases.location

import com.marneux.marneweather.domain.repositories.location.CurrentLocationRepository
import com.marneux.marneweather.model.location.SavedLocation
import kotlinx.coroutines.flow.Flow

class ListSavedLocationUseCase(
    private val locationRepository: CurrentLocationRepository
) {
    fun execute(): Flow<List<SavedLocation>> {
        return locationRepository.listSavedLocation()
    }
}
