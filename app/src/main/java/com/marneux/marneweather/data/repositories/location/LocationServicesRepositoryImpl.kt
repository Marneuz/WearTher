package com.marneux.marneweather.data.repositories.location

import com.marneux.marneweather.domain.models.location.LocationAutofillSuggestion

interface LocationServicesRepositoryImpl {
    suspend fun fetchSuggestedPlacesForQuery(query: String): Result<List<LocationAutofillSuggestion>>
}