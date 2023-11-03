package com.marneux.marneweather.data.remote.location.models

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class SuggestionResponse(
    @SerializedName("results") val suggestions: List<Suggestion> = emptyList()
) {



    data class Suggestion(
        @SerializedName("id") val idOfPlace: String,
        @SerializedName("name") val nameOfPlace: String,
        @SerializedName("country") val country: String?,
        @SerializedName("admin1") val state: String?,
        @SerializedName("country_code") val countryCode: String?,
        @SerializedName("latitude")val latitude: String,
        @SerializedName("longitude")val longitude: String
    )
}

val SuggestionResponse.Suggestion.circularCountryFlagUrl: String?
    get() = countryCode?.let {
        "https://open-meteo.com/images/country-flags/$countryCode.svg"
    }
