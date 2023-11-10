package com.marneux.marneweather.domain.repositories.location

interface GeocoderRepository {
    suspend fun getLocationNameForCoordinates(latitude: Double, longitude: Double): Result<String>
}