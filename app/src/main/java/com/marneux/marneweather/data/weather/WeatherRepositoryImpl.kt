package com.marneux.marneweather.data.weather

import com.marneux.marneweather.data.getBodyOrThrowException
import com.marneux.marneweather.data.safeCall
import com.marneux.marneweather.data.weather.database.CurrentWeatherEntity
import com.marneux.marneweather.data.weather.database.WeatherDao
import com.marneux.marneweather.data.weather.mapper.toCurrentWeather
import com.marneux.marneweather.data.weather.mapper.toHourlyForecasts
import com.marneux.marneweather.data.weather.mapper.toPrecipitationProbabilities
import com.marneux.marneweather.data.weather.mapper.toSavedLocation
import com.marneux.marneweather.data.weather.mapper.toSavedWeatherLocationEntity
import com.marneux.marneweather.data.weather.mapper.toSingleWeatherDetailList
import com.marneux.marneweather.data.weather.remote.WeatherClient
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import com.marneux.marneweather.model.location.SavedLocation
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import com.marneux.marneweather.model.weather.CurrentWeather
import com.marneux.marneweather.model.weather.HourlyForecast
import com.marneux.marneweather.model.weather.PrecipitationProbability
import com.marneux.marneweather.model.weather.SingleWeatherDetail
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
    ): Result<CurrentWeather> =
        safeCall {
            val response = weatherClient.getWeatherForCoordinates(latitude, longitude)
            response.getBodyOrThrowException().toCurrentWeather(nameLocation)
        }

    override fun getSavedLocationsListStream(): Flow<List<SavedLocation>> =
        weatherDao.getAllWeatherEntitiesMarkedAsNotDeleted()
            .map { entities -> entities.map(CurrentWeatherEntity::toSavedLocation) }

    override suspend fun saveWeatherLocation(
        nameLocation: String,
        latitude: String,
        longitude: String
    ) {
        safeCall {
            weatherDao.addSavedWeatherEntity(
                CurrentWeatherEntity(
                    nameLocation,
                    latitude,
                    longitude
                )
            )
        }
    }

    override suspend fun deleteWeatherLocationFromSavedItems(briefWeatherLocation: BriefWeatherDetails) {
        weatherDao.markWeatherEntityAsDeleted(briefWeatherLocation.nameLocation)
    }

    override suspend fun permanentlyDeleteWeatherLocationFromSavedItems(briefWeatherLocation: BriefWeatherDetails) {
        weatherDao.deleteSavedWeatherEntity(briefWeatherLocation.toSavedWeatherLocationEntity())
    }

    override suspend fun fetchHourlyPrecipitationProbabilities(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate>
    ): Result<List<PrecipitationProbability>> =
        safeCall {
            val probabilities = weatherClient.getHourlyForecast(
                latitude,
                longitude,
                dateRange.start,
                dateRange.endInclusive
            )
            probabilities.getBodyOrThrowException().toPrecipitationProbabilities()
        }

    override suspend fun fetchHourlyForecasts(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate>
    ): Result<List<HourlyForecast>> =
        safeCall {
            val forecasts = weatherClient.getHourlyForecast(
                latitude,
                longitude,
                dateRange.start,
                dateRange.endInclusive
            )
            forecasts.getBodyOrThrowException().toHourlyForecasts()
        }

    override suspend fun fetchAdditionalWeatherInfoItemsListForCurrentDay(
        latitude: String,
        longitude: String
    ): Result<List<SingleWeatherDetail>> =
        safeCall {
            val today = LocalDate.now()
            val additionalInfo =
                weatherClient.getAdditionalDailyForecastVariables(latitude, longitude, today, today)
            additionalInfo.getBodyOrThrowException().toSingleWeatherDetailList()
        }

    override suspend fun tryRestoringDeletedWeatherLocation(nameLocation: String) {
        weatherDao.markWeatherEntityAsUnDeleted(nameLocation)
    }


}
