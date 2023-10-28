package com.marneux.marneweather.domain.models.location

import com.marneux.marneweather.data.local.weather.SavedWeatherLocationEntity

data class SavedLocation(
    val nameLocation: String,
    val coordinates: Coordinates
)

fun SavedWeatherLocationEntity.toSavedLocation() = SavedLocation(
    nameLocation = nameLocation,
    coordinates = Coordinates(latitude = latitude, longitude = longitude)
)

