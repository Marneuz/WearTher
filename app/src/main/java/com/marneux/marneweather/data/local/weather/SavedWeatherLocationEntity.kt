package com.marneux.marneweather.data.local.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedWeatherLocations")
data class SavedWeatherLocationEntity(
    @PrimaryKey val nameLocation: String,
    val latitude: String,
    val longitude: String,
    val isDeleted: Boolean = false

)
