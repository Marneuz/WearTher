package com.marneux.marneweather.data.local.textgeneration

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GeneratedTextForLocationEntities")
data class GeneratedTextForLocationEntity(
    @PrimaryKey val nameLocation: String,
    val temperature: Int,
    val conciseWeatherDescription: String,
    val generatedDescription: String,
)
