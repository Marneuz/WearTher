package com.marneux.marneweather.data.weather.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.marneux.marneweather.data.local.database.Database
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM ${Database.SAVED_WEATHER_LOCATION_TABLE_NAME} WHERE is_deleted = 0")
    fun getAllWeatherEntitiesMarkedAsNotDeleted(): Flow<List<SavedWeatherLocationEntity>>

    @Query("SELECT * FROM ${Database.SAVED_WEATHER_LOCATION_TABLE_NAME}")
    fun getAllWeatherEntitiesIrrespectiveOfDeletedStatus(): Flow<List<SavedWeatherLocationEntity>>

    @Upsert
    suspend fun addSavedWeatherEntity(weatherLocationEntity: SavedWeatherLocationEntity)

    @Query(
        "UPDATE ${Database.SAVED_WEATHER_LOCATION_TABLE_NAME} SET is_deleted = 1 WHERE " +
            "name_location = :nameOfWeatherLocationEntity")
    suspend fun markWeatherEntityAsDeleted(nameOfWeatherLocationEntity: String)

    @Query("UPDATE ${Database.SAVED_WEATHER_LOCATION_TABLE_NAME} SET is_deleted = 0 WHERE " +
            "name_location = :nameOfWeatherLocationEntity")
    suspend fun markWeatherEntityAsUnDeleted(nameOfWeatherLocationEntity: String)

    @Query("DELETE FROM ${Database.SAVED_WEATHER_LOCATION_TABLE_NAME} WHERE is_deleted = 1")
    suspend fun deleteAllItemsMarkedAsDeleted()

    @Delete
    suspend fun deleteSavedWeatherEntity(weatherLocationEntity: SavedWeatherLocationEntity)
}