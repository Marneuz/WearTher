package com.marneux.marneweather.ui.activities

import android.Manifest
import android.content.Context
import androidx.core.content.ContextCompat

fun Context.hasLocationPermission(): Boolean {
    val isCoarseLocationPermissionGranted = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == 1
    val isFineLocationPermissionGranted = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == 1
    return isCoarseLocationPermissionGranted || isFineLocationPermissionGranted
}
