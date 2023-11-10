package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.data.weather.mapper.toBriefWeatherDetails
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import com.marneux.marneweather.model.weather.BriefWeatherDetails

class GetBriefWeatherDetailsUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(
        nameLocation: String,
        latitude: String,
        longitude: String
    )
            : Result<BriefWeatherDetails> {
        return try {
            val currentWeatherResult =
                weatherRepository.fetchWeatherForLocation(nameLocation, latitude, longitude)
            currentWeatherResult.map { it.toBriefWeatherDetails() }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
