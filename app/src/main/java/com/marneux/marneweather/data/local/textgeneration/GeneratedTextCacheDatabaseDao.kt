package com.marneux.marneweather.data.local.textgeneration

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface GeneratedTextCacheDatabaseDao {

    @Upsert
    suspend fun addGeneratedTextForLocation(generatedTextForLocationEntity: GeneratedTextForLocationEntity)

    @Query(
        """
    SELECT * 
    FROM ${GeneratedTextCacheDatabase.GENERATED_TEXT_TABLE_NAME} 
    WHERE name_location = :nameLocation AND
          temperature = :temperature AND
          concise_weather_description = :conciseWeatherDescription
    """
    )
    suspend fun getSavedGeneratedTextForDetails(
        nameLocation: String,
        temperature: Int,
        conciseWeatherDescription: String
    ): GeneratedTextForLocationEntity?

    @Query("DELETE from ${GeneratedTextCacheDatabase.GENERATED_TEXT_TABLE_NAME}")
    suspend fun deleteAllSavedText()
}