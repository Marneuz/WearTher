package com.marneux.marneweather.domain.repositories.location

import com.marneux.marneweather.model.location.LocationAutofillSuggestion

interface LocationRepository {
    suspend fun fetchSuggestedPlacesForQuery(query: String): Result<List<LocationAutofillSuggestion>>


}