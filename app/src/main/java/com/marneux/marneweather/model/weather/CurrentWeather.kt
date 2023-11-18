package com.marneux.marneweather.model.weather

import com.marneux.marneweather.model.location.Coordinates

data class CurrentWeather(
    val nameLocation: String,
    val temperatureRoundedToInt: Int,
    val shortDescriptionCode: Int,
    val isDay: Int,
    val coordinates: Coordinates
)

