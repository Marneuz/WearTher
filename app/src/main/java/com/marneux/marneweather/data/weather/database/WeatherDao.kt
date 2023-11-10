package com.marneux.marneweather.data.weather.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.marneux.marneweather.data.local.database.Database
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM ${Database.CURRENT_WEATHER_TABLE_NAME} WHERE is_deleted = 0")
    fun getAllWeatherEntitiesMarkedAsNotDeleted(): Flow<List<CurrentWeatherEntity>>

    @Query("SELECT * FROM ${Database.CURRENT_WEATHER_TABLE_NAME}")
    fun getAllWeatherEntitiesIrrespectiveOfDeletedStatus(): Flow<List<CurrentWeatherEntity>>

    @Upsert
    suspend fun addSavedWeatherEntity(weatherLocationEntity: CurrentWeatherEntity)

    @Query(
        "UPDATE ${Database.CURRENT_WEATHER_TABLE_NAME} SET is_deleted = 1 WHERE " +
                "name_location = :nameOfWeatherLocationEntity"
    )
    suspend fun markWeatherEntityAsDeleted(nameOfWeatherLocationEntity: String)

    @Query(
        "UPDATE ${Database.CURRENT_WEATHER_TABLE_NAME} SET is_deleted = 0 WHERE " +
                "name_location = :nameOfWeatherLocationEntity"
    )
    suspend fun markWeatherEntityAsUnDeleted(nameOfWeatherLocationEntity: String)

    @Query("DELETE FROM ${Database.CURRENT_WEATHER_TABLE_NAME} WHERE is_deleted = 1")
    suspend fun deleteAllItemsMarkedAsDeleted()

    @Delete
    suspend fun deleteSavedWeatherEntity(weatherLocationEntity: CurrentWeatherEntity)
}