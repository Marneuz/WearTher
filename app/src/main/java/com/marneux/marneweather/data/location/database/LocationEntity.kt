package com.marneux.marneweather.data.location.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marneux.marneweather.data.local.database.Database

@Entity(tableName = Database.LOCATION_TABLE_NAME)
data class LocationEntity(
    @PrimaryKey
    @ColumnInfo("name_location") val nameLocation: String,
    @ColumnInfo("latitude") val latitude: String,
    @ColumnInfo("longitude") val longitude: String,
    @ColumnInfo("is_deleted") val isDeleted: Boolean = false
)
