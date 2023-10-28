package com.marneux.marneweather.data.repositories.location

import com.marneux.marneweather.data.getBodyOrThrowException
import com.marneux.marneweather.data.remote.location.LocationClient
import com.marneux.marneweather.domain.models.location.LocationAutofillSuggestion
import com.marneux.marneweather.domain.models.location.toLocationAutofillSuggestionList
import kotlinx.coroutines.CancellationException

class LocationServicesRepository (
    private val locationClient: LocationClient
) : LocationServicesRepositoryImpl {
    override suspend fun fetchSuggestedPlacesForQuery(query: String): Result<List<LocationAutofillSuggestion>> {
        return try {
            if (query.isBlank()) return Result.success(emptyList())
            val suggestions = locationClient.getPlacesSuggestionsForQuery(query = query)
                .getBodyOrThrowException()
                .suggestions
                .toLocationAutofillSuggestionList()
            Result.success(suggestions)
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            Result.failure(exception)
        }
    }
}