package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import com.marneux.marneweather.model.weather.SingleWeatherDetail

class TodayWeatherInfoUseCase(
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