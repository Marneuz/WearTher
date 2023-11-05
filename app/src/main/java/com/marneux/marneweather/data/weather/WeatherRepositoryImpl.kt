package com.marneux.marneweather.data.weather

import com.marneux.marneweather.data.getBodyOrThrowException
import com.marneux.marneweather.data.weather.database.SavedWeatherLocationEntity
import com.marneux.marneweather.data.weather.database.WeatherDao
import com.marneux.marneweather.data.weather.remote.WeatherClient
import com.marneux.marneweather.data.weather.remote.mapper.toCurrentWeather
import com.marneux.marneweather.domain.cajondesastre.location.models.location.SavedLocation
import com.marneux.marneweather.domain.cajondesastre.location.models.location.toSavedLocation
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.BriefWeatherDetails
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.CurrentWeather
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.HourlyForecast
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.PrecipitationProbability
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.SingleWeatherDetail
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.toHourlyForecasts
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.toPrecipitationProbabilities
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.toSavedWeatherLocationEntity
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.toSingleWeatherDetailList
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate


class WeatherRepositoryImpl(
    private val weatherClient: WeatherClient,
    private val weatherDao: WeatherDao
) : WeatherRepository {
    override suspend fun fetchWeatherForLocation(
        nameLocation: String,
        latitude: String,
        longitude: String
    ): Result<CurrentWeather> = try {
        val response = weatherClient.getWeatherForCoordinates(
            latitude = latitude,
            longitude = longitude
        )
        Result.success(response.getBodyOrThrowException().toCurrentWeather(nameLocation))
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        Result.failure(exception)
    }

    override fun getSavedLocationsListStream(): Flow<List<SavedLocation>> = weatherDao
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
        weatherDao.addSavedWeatherEntity(savedWeatherEntity)
    }

    override suspend fun deleteWeatherLocationFromSavedItems(briefWeatherLocation: BriefWeatherDetails) {
        val savedLocationEntity = briefWeatherLocation.toSavedWeatherLocationEntity()
        weatherDao.markWeatherEntityAsDeleted(savedLocationEntity.nameLocation)
    }

    override suspend fun permanentlyDeleteWeatherLocationFromSavedItems(briefWeatherLocation: BriefWeatherDetails) {
        briefWeatherLocation.toSavedWeatherLocationEntity().run {
            weatherDao.deleteSavedWeatherEntity(this)
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
        weatherDao.markWeatherEntityAsUnDeleted(nameLocation)
    }
}