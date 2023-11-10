package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import com.marneux.marneweather.model.weather.HourlyForecast
import java.time.LocalDate
import java.time.LocalDateTime

class FetchHourlyForecastsForNext24HoursUseCase(
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
                val currentTime = LocalDateTime.now()
                val isSameDay = it.dateTime.toLocalDate() == currentTime.toLocalDate()
                if (isSameDay) it.dateTime.toLocalTime() >= currentTime.toLocalTime()
                else it.dateTime > currentTime
            }.take(24)
            Result.success(hourlyForecastsForNext24Hours)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}