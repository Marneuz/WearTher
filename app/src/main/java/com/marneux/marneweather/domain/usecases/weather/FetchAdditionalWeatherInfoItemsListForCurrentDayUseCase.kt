package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.cajondesastre.location.models.weather.SingleWeatherDetail
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository

class FetchAdditionalWeatherInfoItemsListForCurrentDayUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend fun execute(
        latitude: String,
        longitude: String
    ): Result<List<SingleWeatherDetail>> {
        return weatherRepository.fetchAdditionalWeatherInfoItemsListForCurrentDay(
            latitude,
            longitude,
        )
    }
}