package com.marneux.marneweather.model.weather

import java.time.LocalDateTime

data class RainChances(
    val latitude: String,
    val longitude: String,
    val dateTime: LocalDateTime,
    val probabilityPercentage: Int
)



