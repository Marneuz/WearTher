package com.marneux.marneweather.di

import android.content.Context
import androidx.room.Room
import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextDatabaseDao
import com.marneux.marneweather.data.local.database.Database
import com.marneux.marneweather.data.weather.database.WeatherDao
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    singleOf(::provideWeatherDao)
    singleOf(::provideGeneratedTextDao)
}


private fun provideWeatherDao(
    context: Context
): WeatherDao = Room.databaseBuilder(
    context = context,
    klass = Database::class.java,
    name = Database.DATABASE_NAME
).build().getWeatherDao()


private fun provideGeneratedTextDao(
    context: Context
): GeneratedTextDatabaseDao = Room.databaseBuilder(
    context = context,
    klass = Database::class.java,
    name = Database.DATABASE_NAME
).build().getGeneratedTextDao()
