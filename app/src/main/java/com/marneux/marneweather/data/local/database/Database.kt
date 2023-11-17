package com.marneux.marneweather.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextDao
import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextEntity
import com.marneux.marneweather.data.location.database.LocationDao
import com.marneux.marneweather.data.location.database.LocationEntity

@Database(
    entities = [
        LocationEntity::class,
        GeneratedTextEntity::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun getGeneratedTextDao(): GeneratedTextDao
    abstract fun getWeatherDao(): LocationDao

    companion object {
        const val DATABASE_NAME = "weather_database"
        const val LOCATION_TABLE_NAME = "current_locations"
        const val GENERATED_TEXT_TABLE_NAME = "generated_text_for_location"
    }
}


