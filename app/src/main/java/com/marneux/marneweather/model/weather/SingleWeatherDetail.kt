package com.marneux.marneweather.model.weather

import androidx.annotation.DrawableRes

data class SingleWeatherDetail(
    val name: String,
    val value: String,
    @DrawableRes val iconResId: Int
)

