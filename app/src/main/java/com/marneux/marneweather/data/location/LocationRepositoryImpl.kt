package com.marneux.marneweather.data.location

import com.marneux.marneweather.data.getBodyOrThrowException
import com.marneux.marneweather.data.handleRepositoryException
import com.marneux.marneweather.data.location.mapper.toLocationAutofillSuggestionList
import com.marneux.marneweather.data.location.remote.LocationClient
import com.marneux.marneweather.domain.repositories.location.LocationRepository
import com.marneux.marneweather.model.location.LocationAutofillSuggestion


class LocationRepositoryImpl(
    private val locationClient: LocationClient
) : LocationRepository {

    override suspend fun fetchSuggestedPlacesForQuery(query: String): Result<List<LocationAutofillSuggestion>> {
        if (query.isBlank()) {
            return Result.success(emptyList())
        }

        return try {
            val suggestions = locationClient.getPlacesSuggestionsForQuery(query = query)
                .getBodyOrThrowException()
                .suggestions
                .toLocationAutofillSuggestionList()
            Result.success(suggestions)
        } catch (exception: Exception) {
            exception.handleRepositoryException()
        }
    }
}
