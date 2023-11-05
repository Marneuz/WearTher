package com.marneux.marneweather.data.location

import com.marneux.marneweather.data.getBodyOrThrowException
import com.marneux.marneweather.data.location.remote.LocationClient
import com.marneux.marneweather.domain.cajondesastre.location.models.location.LocationAutofillSuggestion
import com.marneux.marneweather.domain.cajondesastre.location.models.location.toLocationAutofillSuggestionList
import com.marneux.marneweather.domain.repositories.location.LocationServicesRepository
import kotlinx.coroutines.CancellationException

class LocationServicesRepositoryImpl(
    private val locationClient: LocationClient
) : LocationServicesRepository {
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