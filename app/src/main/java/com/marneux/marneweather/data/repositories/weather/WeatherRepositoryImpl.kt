package com.marneux.marneweather.data.repositories.weather

import com.marneux.marneweather.domain.models.location.SavedLocation
import com.marneux.marneweather.domain.models.weather.BriefWeatherDetails
import com.marneux.marneweather.domain.models.weather.CurrentWeatherDetails
import com.marneux.marneweather.domain.models.weather.HourlyForecast
import com.marneux.marneweather.domain.models.weather.PrecipitationProbability
import com.marneux.marneweather.domain.models.weather.SingleWeatherDetail
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


interface WeatherRepositoryImpl {

    suspend fun fetchWeatherForLocation(
        nameLocation: String,
        latitude: String,
        longitude: String
    ): Result<CurrentWeatherDetails>

    fun getSavedLocationsListStream(): Flow<List<SavedLocation>>

    suspend fun saveWeatherLocation(
        nameLocation: String,
        latitude: String,
        longitude: String)

    suspend fun deleteWeatherLocationFromSavedItems(briefWeatherLocation: BriefWeatherDetails)

    suspend fun permanentlyDeleteWeatherLocationFromSavedItems(briefWeatherLocation: BriefWeatherDetails)

    suspend fun tryRestoringDeletedWeatherLocation(nameLocation: String)

    suspend fun fetchHourlyPrecipitationProbabilities(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate> = LocalDate.now()..LocalDate.now().plusDays(1)
    ): Result<List<PrecipitationProbability>>

    suspend fun fetchHourlyForecasts(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate>
    ): Result<List<HourlyForecast>>

    suspend fun fetchAdditionalWeatherInfoItemsListForCurrentDay(
        latitude: String,
        longitude: String
    ): Result<List<SingleWeatherDetail>>
}