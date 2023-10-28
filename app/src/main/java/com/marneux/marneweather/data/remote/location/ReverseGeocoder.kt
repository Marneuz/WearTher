package com.marneux.marneweather.data.remote.location

fun interface ReverseGeocoder {
    suspend fun getLocationNameForCoordinates(latitude: Double, longitude: Double): Result<String>
}
