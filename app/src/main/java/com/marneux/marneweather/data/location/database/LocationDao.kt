package com.marneux.marneweather.data.location.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.marneux.marneweather.data.local.database.Database
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM ${Database.LOCATION_TABLE_NAME} WHERE is_deleted = 0")
    fun getAllLocationEntitiesNotDeleted(): Flow<List<LocationEntity>>

    @Upsert
    suspend fun addSavedWeatherEntity(locationEntity: LocationEntity)

    @Query(
        "UPDATE ${Database.LOCATION_TABLE_NAME} SET is_deleted = 1 WHERE " +
                "name_location = :nameOfWeatherLocationEntity"
    )
    suspend fun markLocationDeleted(nameOfWeatherLocationEntity: String)

    @Query(
        "UPDATE ${Database.LOCATION_TABLE_NAME} SET is_deleted = 0 WHERE " +
                "name_location = :nameOfWeatherLocationEntity"
    )
    suspend fun markLocationUndeleted(nameOfWeatherLocationEntity: String)

    @Query("DELETE FROM ${Database.LOCATION_TABLE_NAME} WHERE is_deleted = 1")
    suspend fun deleteAllItemsMarkedAsDeleted()
}