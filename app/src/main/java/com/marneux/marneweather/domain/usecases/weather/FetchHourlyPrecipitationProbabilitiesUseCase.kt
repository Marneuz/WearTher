package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.cajondesastre.location.models.weather.PrecipitationProbability
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import java.time.LocalDate

class FetchHourlyPrecipitationProbabilitiesUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend fun execute(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate> = LocalDate.now()..LocalDate.now().plusDays(1)
    ): Result<List<PrecipitationProbability>> {
        return weatherRepository.fetchHourlyPrecipitationProbabilities(
            latitude,
            longitude,
            dateRange
        )
    }

}
