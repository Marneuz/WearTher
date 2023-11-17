package com.marneux.marneweather.domain.repositories.location

import com.marneux.marneweather.model.location.Coordinates
import com.marneux.marneweather.model.location.SavedLocation
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import kotlinx.coroutines.flow.Flow

interface CurrentLocationRepository {
    suspend fun getCurrentLocation(): Result<Coordinates>

    fun listSavedLocation(): Flow<List<SavedLocation>>

    suspend fun saveWeatherLocation(nameLocation: String, latitude: String, longitude: String)

    suspend fun deleteSavedLocation(briefWeatherLocation: BriefWeatherDetails)

    suspend fun restoreDeletedLocation(nameLocation: String)
}