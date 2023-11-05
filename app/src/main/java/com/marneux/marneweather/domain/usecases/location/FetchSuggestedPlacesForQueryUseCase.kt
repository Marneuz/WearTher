package com.marneux.marneweather.domain.usecases.location

import com.marneux.marneweather.domain.cajondesastre.location.models.location.LocationAutofillSuggestion
import com.marneux.marneweather.domain.repositories.location.LocationServicesRepository

class FetchSuggestedPlacesForQueryUseCase(
    private val locationServicesRepository: LocationServicesRepository
) {
    suspend fun execute(
        query: String
    ): Result<List<LocationAutofillSuggestion>> {
        return locationServicesRepository.fetchSuggestedPlacesForQuery(
            query
        )
    }
}