package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import com.marneux.marneweather.model.weather.CurrentWeather

class WeatherByLocationUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend fun execute(
        nameLocation: String,
        latitude: String,
        longitude: String
    ): Result<CurrentWeather> {
        return weatherRepository.fetchWeatherForLocation(
            nameLocation,
            latitude,
            longitude
        )
    }
}