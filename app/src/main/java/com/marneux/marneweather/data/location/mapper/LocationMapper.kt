package com.marneux.marneweather.data.location.mapper

import com.marneux.marneweather.data.location.remote.models.SuggestionResponse
import com.marneux.marneweather.data.location.remote.models.circularCountryFlagUrl
import com.marneux.marneweather.model.location.Coordinates
import com.marneux.marneweather.model.location.LocationAutofillSuggestion

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