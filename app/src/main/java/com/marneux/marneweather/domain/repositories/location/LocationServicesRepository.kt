package com.marneux.marneweather.domain.repositories.location

import com.marneux.marneweather.domain.cajondesastre.location.models.location.LocationAutofillSuggestion

interface LocationServicesRepository {
    suspend fun fetchSuggestedPlacesForQuery(query: String): Result<List<LocationAutofillSuggestion>>
}