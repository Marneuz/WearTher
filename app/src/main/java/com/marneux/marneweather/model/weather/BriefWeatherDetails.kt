package com.marneux.marneweather.model.weather

import com.marneux.marneweather.model.location.Coordinates

data class BriefWeatherDetails(
    val nameLocation: String,
    val temperatureRoundedToInt: Int,
    val shortDescriptionCode: Int,
    val coordinates: Coordinates
)
