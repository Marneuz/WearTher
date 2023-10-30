package com.marneux.marneweather.data.local.weather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saved_weather_locations")
data class SavedWeatherLocationEntity(
    @PrimaryKey
    @ColumnInfo("name_location") val nameLocation: String,
    @ColumnInfo("latitude") val latitude: String,
    @ColumnInfo("longitude") val longitude: String,
    @ColumnInfo("is_deleted") val isDeleted: Boolean = false

)
