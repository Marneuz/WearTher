package com.marneux.marneweather.data.location

import android.content.Context
import android.location.Geocoder
import com.marneux.marneweather.data.safeCall
import com.marneux.marneweather.domain.repositories.location.GeocoderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Locale

class GeocoderImpl(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : GeocoderRepository {

    override suspend fun getLocationNameForCoordinates(
        latitude: Double,
        longitude: Double
    ): Result<String> = withContext(ioDispatcher) {
        safeCall {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val address = addresses?.firstOrNull()
                ?: throw Exception("No address found for the given coordinates")
            "${address.locality}, ${address.adminArea}"
        }
    }
}