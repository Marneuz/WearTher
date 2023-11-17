package com.marneux.marneweather.data.location.remote.models

import com.google.gson.annotations.SerializedName


data class SuggestionResponse(
    @SerializedName("results") val suggestions: List<Suggestion> = emptyList()
) {

    data class Suggestion(
        @SerializedName("id") val idLocation: String,
        @SerializedName("name") val nameLocation: String,
        @SerializedName("country") val country: String?,
        @SerializedName("admin1") val state: String?,
        @SerializedName("country_code") val countryCode: String?,
        @SerializedName("latitude") val latitude: String,
        @SerializedName("longitude") val longitude: String
    )
}

val SuggestionResponse.Suggestion.circularCountryFlagUrl: String?
    get() = countryCode?.let {
        "https://open-meteo.com/images/country-flags/$countryCode.svg"
    }
