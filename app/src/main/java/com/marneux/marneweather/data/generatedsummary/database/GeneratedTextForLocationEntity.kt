package com.marneux.marneweather.data.generatedsummary.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marneux.marneweather.data.local.database.Database

@Entity(tableName = Database.GENERATED_TEXT_TABLE_NAME)
data class GeneratedTextForLocationEntity(
    @PrimaryKey
    @ColumnInfo("name_location") val nameLocation: String,
    @ColumnInfo("temperature") val temperature: Int,
    @ColumnInfo("concise_weather_description") val conciseWeatherDescription: String,
    @ColumnInfo("generated_description") val generatedDescription: String,
)
