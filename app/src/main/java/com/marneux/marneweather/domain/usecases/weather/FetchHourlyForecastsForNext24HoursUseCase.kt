package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.cajondesastre.location.models.weather.HourlyForecast
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import com.marneux.marneweather.domain.repositories.weather.WeatherRepositoryExtensions
import kotlinx.coroutines.CancellationException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class FetchHourlyForecastsForNext24HoursUseCase(
    private val weatherRepositoryExtension: WeatherRepositoryExtensions,
    private val weatherRepository: WeatherRepository
) {
    suspend fun execute(
        latitude: String,
        longitude: String,
    ): Result<List<HourlyForecast>> {
        return try {
            val hourlyForecastsForNext24Hours = weatherRepository.fetchHourlyForecasts(
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
}