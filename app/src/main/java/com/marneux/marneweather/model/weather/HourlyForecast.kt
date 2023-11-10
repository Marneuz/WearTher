package com.marneux.marneweather.model.weather

import androidx.annotation.DrawableRes
import java.time.LocalDateTime

data class HourlyForecast(
    val dateTime: LocalDateTime,
    @DrawableRes val weatherIconResId: Int,
    val temperature: Int
)

