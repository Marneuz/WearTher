package com.marneux.marneweather.domain.repositories.location

interface GeocoderRepository {
    suspend fun getLocationName(latitude: Double, longitude: Double): Result<String>
}