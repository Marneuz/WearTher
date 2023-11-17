package com.marneux.marneweather.data.location

import com.marneux.marneweather.data.getBodyOrThrowException
import com.marneux.marneweather.data.handleRepositoryException
import com.marneux.marneweather.data.location.mapper.toAutoSuggestLocationList
import com.marneux.marneweather.data.location.remote.LocationClient
import com.marneux.marneweather.domain.repositories.location.LocationRepository
import com.marneux.marneweather.model.location.AutoSuggestLocation


class LocationRepositoryImpl(
    private val locationClient: LocationClient
) : LocationRepository {

    override suspend fun fetchSuggestedLocationQuery(query: String): Result<List<AutoSuggestLocation>> {
        if (query.isBlank()) {
            return Result.success(emptyList())
        }
        return try {
            val suggestions = locationClient.getLocationSuggestionsQuery(query = query)
                .getBodyOrThrowException()
                .suggestions
                .toAutoSuggestLocationList()
            Result.success(suggestions)
        } catch (exception: Exception) {
            exception.handleRepositoryException()
        }
    }
}
