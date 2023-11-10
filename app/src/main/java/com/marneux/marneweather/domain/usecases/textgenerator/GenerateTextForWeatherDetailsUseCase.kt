package com.marneux.marneweather.domain.usecases.textgenerator

import com.marneux.marneweather.domain.repositories.textgenerator.GenerativeTextRepository
import com.marneux.marneweather.model.weather.CurrentWeather

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