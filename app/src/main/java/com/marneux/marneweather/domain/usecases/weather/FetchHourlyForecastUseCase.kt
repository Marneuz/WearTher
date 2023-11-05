package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.cajondesastre.location.models.weather.HourlyForecast
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import java.time.LocalDate

class FetchHourlyForecastUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend fun execute(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate>
    ): Result<List<HourlyForecast>> {
        return weatherRepository.fetchHourlyForecasts(
            latitude,
            longitude,
            dateRange
        )
    }
}