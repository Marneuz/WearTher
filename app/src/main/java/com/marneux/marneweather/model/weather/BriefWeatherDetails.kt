package com.marneux.marneweather.model.weather

import androidx.annotation.DrawableRes
import com.marneux.marneweather.model.location.Coordinates

data class BriefWeatherDetails(
    val nameLocation: String,
    val currentTemperatureRoundedToInt: Int,
    val shortDescription: String,
    @DrawableRes val shortDescriptionIcon: Int,
    val coordinates: Coordinates
)
