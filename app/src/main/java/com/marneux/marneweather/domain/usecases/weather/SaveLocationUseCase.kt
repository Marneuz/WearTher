package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.repositories.weather.WeatherRepository

class SaveLocationUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend fun execute(
        nameLocation: String,
        latitude: String,
        longitude: String
    ) {
        return weatherRepository.saveWeatherLocation(
            nameLocation,
            latitude,
            longitude
        )
    }
}



