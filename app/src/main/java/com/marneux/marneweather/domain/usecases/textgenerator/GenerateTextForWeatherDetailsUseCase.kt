package com.marneux.marneweather.domain.usecases.textgenerator

import com.marneux.marneweather.domain.cajondesastre.location.models.weather.CurrentWeather
import com.marneux.marneweather.domain.repositories.textgenerator.GenerativeTextRepository

class GenerateTextForWeatherDetailsUseCase(
    private val generativeTextRepository: GenerativeTextRepository
) {
    suspend fun execute(
        weatherDetails: CurrentWeather
    ): Result<String> {
        return generativeTextRepository.generateTextForWeatherDetails(
            weatherDetails
        )
    }
}