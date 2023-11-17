package com.marneux.marneweather.model.location


data class AutoSuggestLocation(
    val idLocation: String,
    val nameLocation: String,
    val addressLocation: String,
    val coordinatesLocation: Coordinates,
    val countryFlag: String
)

