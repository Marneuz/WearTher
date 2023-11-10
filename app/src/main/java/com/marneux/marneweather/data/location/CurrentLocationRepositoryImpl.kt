package com.marneux.marneweather.data.location

import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.marneux.marneweather.domain.repositories.location.CurrentLocationRepository
import com.marneux.marneweather.model.location.Coordinates
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await


class CurrentLocationRepositoryImpl(
    private val context: Context
) : CurrentLocationRepository {

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