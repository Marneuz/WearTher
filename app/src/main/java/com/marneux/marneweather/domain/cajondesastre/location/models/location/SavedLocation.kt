package com.marneux.marneweather.domain.cajondesastre.location.models.location

import com.marneux.marneweather.data.weather.database.SavedWeatherLocationEntity

data class SavedLocation(
    val nameLocation: String,
    val coordinates: Coordinates
)

fun SavedWeatherLocationEntity.toSavedLocation() = SavedLocation(
    nameLocation = nameLocation,
    coordinates = Coordinates(latitude = latitude, longitude = longitude)
)

