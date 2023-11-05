package com.marneux.marneweather.domain.cajondesastre.location.models.weather

import androidx.annotation.DrawableRes
import com.marneux.marneweather.data.weather.database.SavedWeatherLocationEntity
import com.marneux.marneweather.domain.cajondesastre.location.models.location.Coordinates

data class BriefWeatherDetails(
    val nameLocation: String,
    val currentTemperatureRoundedToInt: Int,
    val shortDescription: String,
    @DrawableRes val shortDescriptionIcon: Int,
    val coordinates: Coordinates
)

fun BriefWeatherDetails.toSavedWeatherLocationEntity(): SavedWeatherLocationEntity =
    SavedWeatherLocationEntity(
        nameLocation = this.nameLocation,
        latitude = this.coordinates.latitude,
        longitude = this.coordinates.longitude
    )