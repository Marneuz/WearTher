package com.marneux.marneweather.model.weather

import java.time.LocalDateTime

data class HourlyForecast(
    val dateTime: LocalDateTime,
    val temperature: Int,
    val iconDescriptionCode: Int,
)

