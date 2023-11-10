package com.marneux.marneweather.model.location


data class LocationAutofillSuggestion(
    val idLocation: String,
    val nameLocation: String,
    val addressLocation: String,
    val coordinatesLocation: Coordinates,
    val countryFlag: String
)

