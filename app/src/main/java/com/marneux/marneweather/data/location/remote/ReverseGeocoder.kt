package com.marneux.marneweather.data.location.remote

fun interface ReverseGeocoder {
    suspend fun getLocationNameForCoordinates(latitude: Double, longitude: Double): Result<String>
}
