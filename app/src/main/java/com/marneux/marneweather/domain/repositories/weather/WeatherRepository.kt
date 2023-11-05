package com.marneux.marneweather.domain.repositories.weather

import com.marneux.marneweather.domain.cajondesastre.location.models.location.SavedLocation
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.BriefWeatherDetails
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.CurrentWeather
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.HourlyForecast
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.PrecipitationProbability
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.SingleWeatherDetail
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


interface WeatherRepository {

    suspend fun fetchWeatherForLocation(
        nameLocation: String,
        latitude: String,
        longitude: String
    ): Result<CurrentWeather>

    fun getSavedLocationsListStream(): Flow<List<SavedLocation>>

    suspend fun saveWeatherLocation(nameLocation: String, latitude: String, longitude: String)

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
