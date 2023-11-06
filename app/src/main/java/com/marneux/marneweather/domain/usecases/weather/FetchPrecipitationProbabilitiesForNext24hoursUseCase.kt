package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.cajondesastre.location.models.weather.PrecipitationProbability
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import kotlinx.coroutines.CancellationException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class FetchPrecipitationProbabilitiesForNext24hoursUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend fun execute(
        latitude: String,
        longitude: String,
    ): Result<List<PrecipitationProbability>> {
        return try {
            val probabilitiesForNext24hours =
                weatherRepository.fetchHourlyPrecipitationProbabilities(
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
}