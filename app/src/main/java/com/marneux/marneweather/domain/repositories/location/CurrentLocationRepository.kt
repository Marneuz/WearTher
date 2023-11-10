package com.marneux.marneweather.domain.repositories.location

import com.marneux.marneweather.model.location.Coordinates

interface CurrentLocationRepository {
    suspend fun getCurrentLocation(): Result<Coordinates>
}