package com.marneux.marneweather.domain.usecases.location

import com.marneux.marneweather.domain.repositories.location.LocationRepository
import com.marneux.marneweather.model.location.LocationAutofillSuggestion

class FetchSuggestedPlacesForQueryUseCase(
    private val locationRepository: LocationRepository
) {
    suspend fun execute(
        query: String
    ): Result<List<LocationAutofillSuggestion>> {
        return locationRepository.fetchSuggestedPlacesForQuery(
            query
        )
    }
}