package com.marneux.marneweather.domain.usecases.location

import com.marneux.marneweather.domain.repositories.location.LocationRepository
import com.marneux.marneweather.model.location.AutoSuggestLocation

class SuggestedLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend fun execute(
        query: String
    ): Result<List<AutoSuggestLocation>> {
        return locationRepository.fetchSuggestedLocationQuery(
            query
        )
    }
}