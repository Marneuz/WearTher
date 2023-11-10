package com.marneux.marneweather.model.weather

import androidx.annotation.DrawableRes
import com.marneux.marneweather.model.location.Coordinates

data class CurrentWeather(
    val nameLocation: String,
    val temperatureRoundedToInt: Int,
    val weatherCondition: String,
    val isDay: Int,
    @DrawableRes val iconResId: Int,
    @DrawableRes val imageResId: Int,
    val coordinates: Coordinates
)

