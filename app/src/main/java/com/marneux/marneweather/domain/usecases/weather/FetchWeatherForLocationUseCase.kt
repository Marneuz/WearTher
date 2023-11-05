package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.cajondesastre.location.models.weather.CurrentWeather
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository

class FetchWeatherForLocationUseCase(
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