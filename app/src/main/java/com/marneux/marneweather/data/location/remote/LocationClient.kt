package com.marneux.marneweather.data.location.remote

import androidx.annotation.IntRange
import com.marneux.marneweather.data.location.remote.models.SuggestionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationClient {

    @GET(LocationClientConstants.EndPoints.GET_PLACES_SUGGESTIONS_FOR_QUERY)
    suspend fun getPlacesSuggestionsForQuery(
        @Query("name") query: String,
        @Query("count") @IntRange(1, 100) count: Int = 20
    ): Response<SuggestionResponse>

}
