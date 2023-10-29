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
    FROM GeneratedTextForLocationEntities
    WHERE nameLocation = :nameLocation AND
          temperature = :temperature AND
          conciseWeatherDescription = :conciseWeatherDescription
    """
    )
    suspend fun getSavedGeneratedTextForDetails(
        nameLocation: String,
        temperature: Int,
        conciseWeatherDescription: String
    ): GeneratedTextForLocationEntity?

    @Query("DELETE from GeneratedTextForLocationEntities")
    suspend fun deleteAllSavedText()
}