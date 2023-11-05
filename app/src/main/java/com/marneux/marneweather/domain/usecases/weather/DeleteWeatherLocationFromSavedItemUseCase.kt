package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.domain.cajondesastre.location.models.weather.BriefWeatherDetails
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository

class DeleteWeatherLocationFromSavedItemUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend fun execute(
        briefWeatherDetails: BriefWeatherDetails
    ) {
        return weatherRepository.deleteWeatherLocationFromSavedItems(
            briefWeatherDetails
        )
    }
}