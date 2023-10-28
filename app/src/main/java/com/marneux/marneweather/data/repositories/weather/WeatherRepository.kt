package com.marneux.marneweather.data.repositories.weather

import com.marneux.marneweather.data.getBodyOrThrowException
import com.marneux.marneweather.data.local.weather.DatabaseDao
import com.marneux.marneweather.data.local.weather.SavedWeatherLocationEntity
import com.marneux.marneweather.data.remote.weather.WeatherClient
import com.marneux.marneweather.domain.models.location.SavedLocation
import com.marneux.marneweather.domain.models.location.toSavedLocation
import com.marneux.marneweather.domain.models.weather.BriefWeatherDetails
import com.marneux.marneweather.domain.models.weather.CurrentWeatherDetails
import com.marneux.marneweather.domain.models.weather.HourlyForecast
import com.marneux.marneweather.domain.models.weather.PrecipitationProbability
import com.marneux.marneweather.domain.models.weather.SingleWeatherDetail
import com.marneux.marneweather.domain.models.weather.toCurrentWeatherDetails
import com.marneux.marneweather.domain.models.weather.toHourlyForecasts
import com.marneux.marneweather.domain.models.weather.toPrecipitationProbabilities
import com.marneux.marneweather.domain.models.weather.toSavedWeatherLocationEntity
import com.marneux.marneweather.domain.models.weather.toSingleWeatherDetailList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate


class WeatherRepository (
    private val weatherClient: WeatherClient,
    private val databaseDao: DatabaseDao
) : WeatherRepositoryImpl {
    override suspend fun fetchWeatherForLocation(
        nameLocation: String,
        latitude: String,
        longitude: String
    ): Result<CurrentWeatherDetails> = try {
        val response = weatherClient.getWeatherForCoordinates(
            latitude = latitude,
            longitude = longitude
        )
        Result.success(response.getBodyOrThrowException().toCurrentWeatherDetails(nameLocation))
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        Result.failure(exception)
    }

    override fun getSavedLocationsListStream(): Flow<List<SavedLocation>> = databaseDao
        .getAllWeatherEntitiesMarkedAsNotDeleted()
        .map { savedLocationEntitiesList -> savedLocationEntitiesList.map { it.toSavedLocation() } }

    override suspend fun saveWeatherLocation(
        nameLocation: String,
        latitude: String,
        longitude: String
    ) {
        val savedWeatherEntity = SavedWeatherLocationEntity(
            nameLocation = nameLocation,
            latitude = latitude,
            longitude = longitude
        )
        databaseDao.addSavedWeatherEntity(savedWeatherEntity)
    }

    override suspend fun deleteWeatherLocationFromSavedItems(briefWeatherLocation: BriefWeatherDetails) {
        val savedLocationEntity = briefWeatherLocation.toSavedWeatherLocationEntity()
        databaseDao.markWeatherEntityAsDeleted(savedLocationEntity.nameLocation)
    }

    override suspend fun permanentlyDeleteWeatherLocationFromSavedItems(briefWeatherLocation: BriefWeatherDetails) {
        briefWeatherLocation.toSavedWeatherLocationEntity().run {
            databaseDao.deleteSavedWeatherEntity(this)
        }
    }

    override suspend fun fetchHourlyPrecipitationProbabilities(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate>
    ): Result<List<PrecipitationProbability>> = try {
        val precipitationProbabilities = weatherClient.getHourlyForecast(
            latitude = latitude,
            longitude = longitude,
            startDate = dateRange.start,
            endDate = dateRange.endInclusive
        ).getBodyOrThrowException().toPrecipitationProbabilities()
        Result.success(precipitationProbabilities)
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        Result.failure(exception)
    }

    override suspend fun fetchHourlyForecasts(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate>
    ): Result<List<HourlyForecast>> = try {
        val hourlyForecasts = weatherClient.getHourlyForecast(
            latitude = latitude,
            longitude = longitude,
            startDate = dateRange.start,
            endDate = dateRange.endInclusive
        ).getBodyOrThrowException().toHourlyForecasts()
        Result.success(hourlyForecasts)
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        Result.failure(exception)
    }

    override suspend fun fetchAdditionalWeatherInfoItemsListForCurrentDay(
        latitude: String,
        longitude: String,
    ): Result<List<SingleWeatherDetail>> = try {
        val additionalWeatherInfoItemsList = weatherClient.getAdditionalDailyForecastVariables(
            latitude = latitude,
            longitude = longitude,
            startDate = LocalDate.now(),
            endDate = LocalDate.now()
        ).getBodyOrThrowException().toSingleWeatherDetailList()
        Result.success(additionalWeatherInfoItemsList)
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        Result.failure(exception)
    }

    override suspend fun tryRestoringDeletedWeatherLocation(nameLocation: String) {
        databaseDao.markWeatherEntityAsUnDeleted(nameLocation)
    }
}