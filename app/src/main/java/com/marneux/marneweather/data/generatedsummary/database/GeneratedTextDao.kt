package com.marneux.marneweather.data.generatedsummary.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.marneux.marneweather.data.local.database.Database

@Dao
interface GeneratedTextDao {

    @Upsert
    suspend fun addGeneratedTextForLocation(
        generatedTextEntity:
        GeneratedTextEntity
    )

    @Query(
        """
    SELECT * 
    FROM ${Database.GENERATED_TEXT_TABLE_NAME} 
    WHERE name_location = :nameLocation AND
          temperature = :temperature AND
          concise_weather_description = :conciseWeatherDescription
    """
    )
    suspend fun getSavedGeneratedTextForDetails(
        nameLocation: String,
        temperature: Int,
        conciseWeatherDescription: String
    ): GeneratedTextEntity?

    @Query("DELETE from ${Database.GENERATED_TEXT_TABLE_NAME}")
    suspend fun deleteAllSavedText()
}