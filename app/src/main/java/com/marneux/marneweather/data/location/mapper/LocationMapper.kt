package com.marneux.marneweather.data.location.mapper

import com.marneux.marneweather.data.location.database.LocationEntity
import com.marneux.marneweather.data.location.remote.models.SuggestionResponse
import com.marneux.marneweather.data.location.remote.models.circularCountryFlagUrl
import com.marneux.marneweather.model.location.AutoSuggestLocation
import com.marneux.marneweather.model.location.Coordinates
import com.marneux.marneweather.model.location.SavedLocation

fun List<SuggestionResponse.Suggestion>.toAutoSuggestLocationList():
        List<AutoSuggestLocation> = this.filter {
    it.state != null && it.country != null && it.circularCountryFlagUrl != null
}
    .map {
        AutoSuggestLocation(
            idLocation = it.idLocation,
            nameLocation = it.nameLocation,
            addressLocation = "${it.state}, ${it.country}",
            coordinatesLocation = Coordinates(
                latitude = it.latitude,
                longitude = it.longitude
            ),
            countryFlag = it.circularCountryFlagUrl!!
        )
    }

// Convierte una entidad LocationEntity en un objeto SavedLocation.
fun LocationEntity.toSavedLocation() = SavedLocation(
    nameLocation = nameLocation,
    coordinates = Coordinates(latitude = latitude, longitude = longitude)
)