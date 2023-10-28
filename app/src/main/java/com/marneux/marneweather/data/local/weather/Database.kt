package com.marneux.marneweather.data.local.weather

import androidx.room.RoomDatabase
import androidx.room.Database

@Database(entities = [SavedWeatherLocationEntity::class], version = 1)
abstract class Database : RoomDatabase() {

        abstract fun getDao(): DatabaseDao

        companion object {
            const val DATABASE_NAME = "Weather_Database"
        }
}