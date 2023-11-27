package com.marneux.marneweather.data.location

import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.marneux.marneweather.data.location.database.LocationDao
import com.marneux.marneweather.data.location.database.LocationEntity
import com.marneux.marneweather.data.location.mapper.toSavedLocation
import com.marneux.marneweather.data.safeCall
import com.marneux.marneweather.domain.repositories.location.CurrentLocationRepository
import com.marneux.marneweather.model.location.Coordinates
import com.marneux.marneweather.model.location.SavedLocation
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await


class CurrentLocationRepositoryImpl(
    private val context: Context,
    private val locationDao: LocationDao
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

    override fun listSavedLocation(): Flow<List<SavedLocation>> =
        locationDao.getAllLocationEntitiesNotDeleted()
            .map { entities -> entities.map(LocationEntity::toSavedLocation) }

    override suspend fun saveWeatherLocation(
        nameLocation: String,
        latitude: String,
        longitude: String
    ) {
        safeCall {
            locationDao.addSavedWeatherEntity(
                LocationEntity(
                    nameLocation,
                    latitude,
                    longitude
                )
            )
        }
    }

    override suspend fun deleteSavedLocation(briefWeatherLocation: BriefWeatherDetails) {
        locationDao.markLocationDeleted(briefWeatherLocation.nameLocation)
    }

    override suspend fun restoreDeletedLocation(nameLocation: String) {
        locationDao.markLocationUndeleted(nameLocation)
    }
}
