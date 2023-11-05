package com.marneux.marneweather.domain.cajondesastre.location.models.locationprovider

import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.marneux.marneweather.domain.cajondesastre.location.models.location.Coordinates
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await


class CurrentLocationProviderImpl(
    private val context: Context
) : CurrentLocationProvider {

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @RequiresPermission(
        anyOf = [android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION]
    )
    override suspend fun getCurrentLocation(): Result<Coordinates> = try {
        val currentLocationRequest = CurrentLocationRequest.Builder()
            .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
        val location =
            fusedLocationProviderClient.getCurrentLocation(currentLocationRequest, null)
                .await()
        val coordinates = Coordinates(
            latitude = location.latitude.toString(),
            longitude = location.longitude.toString()
        )
        Result.success(coordinates)
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        Result.failure(exception)
    }
}