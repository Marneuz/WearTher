package com.marneux.marneweather.domain.repositories.weather

import com.marneux.marneweather.domain.cajondesastre.location.models.weather.HourlyForecast
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.PrecipitationProbability
import kotlinx.coroutines.CancellationException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

suspend fun WeatherRepository.fetchPrecipitationProbabilitiesForNext24hours(
    latitude: String,
    longitude: String,
): Result<List<PrecipitationProbability>> {
    return try {
        val probabilitiesForNext24hours = this.fetchHourlyPrecipitationProbabilities(
            latitude = latitude,
            longitude = longitude,
            dateRange = LocalDate.now()..LocalDate.now().plusDays(1)
        ).getOrThrow().filter {
            val isSameDay = it.dateTime == LocalDateTime.now()
            if (isSameDay) it.dateTime.toLocalTime() >= LocalTime.now()
            else it.dateTime > LocalDateTime.now()
        }.take(24)
        Result.success(probabilitiesForNext24hours)
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        Result.failure(exception)
    }
}

suspend fun WeatherRepository.fetchHourlyForecastsForNext24Hours(
    latitude: String,
    longitude: String,
): Result<List<HourlyForecast>> {
    return try {
        val hourlyForecastsForNext24Hours = this.fetchHourlyForecasts(
            latitude = latitude,
            longitude = longitude,
            dateRange = LocalDate.now()..LocalDate.now().plusDays(1)
        ).getOrThrow().filter {
            val isSameDay = it.dateTime == LocalDateTime.now()
            if (isSameDay) it.dateTime.toLocalTime() >= LocalTime.now()
            else it.dateTime > LocalDateTime.now()
        }.take(24)
        Result.success(hourlyForecastsForNext24Hours)
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        Result.failure(exception)
    }
}