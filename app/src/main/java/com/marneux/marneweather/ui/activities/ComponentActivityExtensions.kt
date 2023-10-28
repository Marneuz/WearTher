package com.marneux.marneweather.ui.activities

import android.Manifest
import android.location.Location
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume

suspend fun ComponentActivity.requestLocationPermission(): Boolean =
    suspendCancellableCoroutine { continuation ->
        val locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                continuation.resume(
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true ||
                            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                )
            }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

@RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
suspend fun ComponentActivity.getCurrentLocation(): Location? {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    val locationRequest = CurrentLocationRequest.Builder()
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()
    return try {
        fusedLocationClient.getCurrentLocation(
            locationRequest,
            null
        ).await()
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        null
    }
}