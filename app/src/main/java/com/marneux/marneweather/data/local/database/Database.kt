package com.marneux.marneweather.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextDatabaseDao
import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextForLocationEntity
import com.marneux.marneweather.data.weather.database.SavedWeatherLocationEntity
import com.marneux.marneweather.data.weather.database.WeatherDao

@Database(
    entities = [
        SavedWeatherLocationEntity::class,
        GeneratedTextForLocationEntity::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun getGeneratedTextDao(): GeneratedTextDatabaseDao
    abstract fun getWeatherDao(): WeatherDao


    companion object {
        const val DATABASE_NAME = "weather_database"
        const val SAVED_WEATHER_LOCATION_TABLE_NAME = "saved_weather_locations"
        const val GENERATED_TEXT_TABLE_NAME = "generated_text_for_location"
    }
}


