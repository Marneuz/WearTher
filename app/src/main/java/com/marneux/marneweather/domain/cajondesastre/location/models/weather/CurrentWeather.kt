package com.marneux.marneweather.domain.cajondesastre.location.models.weather

import androidx.annotation.DrawableRes
import com.marneux.marneweather.domain.cajondesastre.location.models.location.Coordinates

data class CurrentWeather(
    val nameLocation: String,
    val temperatureRoundedToInt: Int,
    val weatherCondition: String,
    val isDay: Int,
    @DrawableRes val iconResId: Int,
    @DrawableRes val imageResId: Int,
    val coordinates: Coordinates
)

