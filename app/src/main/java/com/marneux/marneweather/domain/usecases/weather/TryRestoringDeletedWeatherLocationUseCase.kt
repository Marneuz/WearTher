package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.repositories.weather.WeatherRepository

class TryRestoringDeletedWeatherLocationUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend fun execute(
        nameLocation: String
    ) {
        return weatherRepository.tryRestoringDeletedWeatherLocation(
            nameLocation
        )
    }
}