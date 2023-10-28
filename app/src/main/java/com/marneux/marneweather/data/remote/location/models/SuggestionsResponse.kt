package com.marneux.marneweather.data.remote.location.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SuggestionResponse(@Json(name = "results") val suggestions: List<Suggestion> = emptyList()) {


    @JsonClass(generateAdapter = true)
    data class Suggestion(
        @Json(name = "id") val idOfPlace: String,
        @Json(name = "name") val nameOfPlace: String,
        @Json(name = "country") val country: String?,
        @Json(name = "admin1") val state: String?,
        @Json(name = "country_code") val countryCode: String?,
        val latitude: String,
        val longitude: String
    )
}

val SuggestionResponse.Suggestion.circularCountryFlagUrl: String?
    get() = countryCode?.let {
        "https://open-meteo.com/images/country-flags/$countryCode.svg"
    }
