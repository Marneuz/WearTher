package com.marneux.marneweather.domain.cajondesastre.location.models.locationprovider

import com.marneux.marneweather.domain.cajondesastre.location.models.location.Coordinates

interface CurrentLocationProvider {
    suspend fun getCurrentLocation(): Result<Coordinates>
}