package com.marneux.marneweather.domain.location

import com.marneux.marneweather.domain.models.location.Coordinates

interface CurrentLocationProviderImpl{
    suspend fun getCurrentLocation(): Result<Coordinates>
}