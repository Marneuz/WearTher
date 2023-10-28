package com.marneux.marneweather.data.remote.location

import android.content.Context
import android.location.Geocoder
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Locale

class LocationReverseGeocoder(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : ReverseGeocoder {
    override suspend fun getLocationNameForCoordinates(
        latitude: Double,
        longitude: Double
    ): Result<String> = withContext(ioDispatcher) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())

            /**getFromLocation bloquearia el contexto principal, por eso lo ejecuto desde
             * coroutineDispatcher*/
            val address = geocoder.getFromLocation(latitude, longitude, 1)?.first()!!
            Result.success("${address.locality}, ${address.adminArea}")
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            Result.failure(exception)
        }
    }

}