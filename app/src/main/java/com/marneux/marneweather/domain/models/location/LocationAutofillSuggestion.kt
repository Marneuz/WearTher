package com.marneux.marneweather.domain.models.location

import com.marneux.marneweather.data.remote.location.models.SuggestionResponse
import com.marneux.marneweather.data.remote.location.models.circularCountryFlagUrl


data class LocationAutofillSuggestion(
    val idLocation: String,
    val nameLocation: String,
    val addressLocation: String,
    val coordinatesLocation: Coordinates,
    val countryFlag: String
)

fun List<SuggestionResponse.Suggestion>.toLocationAutofillSuggestionList():
        List<LocationAutofillSuggestion> = this.filter {
    it.state != null && it.country != null && it.circularCountryFlagUrl != null
}
    .map {
        LocationAutofillSuggestion(
            idLocation = it.idOfPlace,
            nameLocation = it.nameOfPlace,
            addressLocation = "${it.state}, ${it.country}",
            coordinatesLocation = Coordinates(
                latitude = it.latitude,
                longitude = it.longitude
            ),
            countryFlag = it.circularCountryFlagUrl!!
        )
    }