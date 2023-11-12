package com.marneux.marneweather.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextDao
import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextEntity
import com.marneux.marneweather.data.weather.database.CurrentWeatherEntity
import com.marneux.marneweather.data.weather.database.WeatherDao

@Database(
    entities = [
        CurrentWeatherEntity::class,
        GeneratedTextEntity::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun getGeneratedTextDao(): GeneratedTextDao
    abstract fun getWeatherDao(): WeatherDao


    companion object {
        const val DATABASE_NAME = "weather_database"
        const val CURRENT_WEATHER_TABLE_NAME = "current_weather_locations"
        const val GENERATED_TEXT_TABLE_NAME = "generated_text_for_location"
    }
}


