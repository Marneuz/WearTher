package com.marneux.marneweather.domain.repositories.location

import com.marneux.marneweather.model.location.AutoSuggestLocation

interface LocationRepository {
    suspend fun fetchSuggestedLocationQuery(query: String): Result<List<AutoSuggestLocation>>
}