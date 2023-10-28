package com.marneux.marneweather.di

import android.content.Context
import androidx.room.Room
import com.marneux.marneweather.data.local.textgeneration.GeneratedTextCacheDatabase
import com.marneux.marneweather.data.local.textgeneration.GeneratedTextCacheDatabaseDao
import com.marneux.marneweather.data.local.weather.Database
import com.marneux.marneweather.data.local.weather.DatabaseDao
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    singleOf(::provideDatabaseDao)
    singleOf(::provideGeneratedTextCacheDatabaseDao)
}


private fun provideDatabaseDao(
    context: Context
): DatabaseDao = Room.databaseBuilder(
    context = context,
    klass = Database::class.java,
    name = Database.DATABASE_NAME
).build().getDao()


private fun provideGeneratedTextCacheDatabaseDao(
    context: Context
): GeneratedTextCacheDatabaseDao = Room.databaseBuilder(
    context = context,
    klass = GeneratedTextCacheDatabase::class.java,
    name = GeneratedTextCacheDatabase.DATABASE_NAME
).build().getDao()
