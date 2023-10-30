package com.marneux.marneweather.data.local.textgeneration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "generated_text_for_location")
data class GeneratedTextForLocationEntity(
    @PrimaryKey
    @ColumnInfo("name_location") val nameLocation: String,
    @ColumnInfo("temperature") val temperature: Int,
    @ColumnInfo("concise_weather_description") val conciseWeatherDescription: String,
    @ColumnInfo("generated_description") val generatedDescription: String,
)