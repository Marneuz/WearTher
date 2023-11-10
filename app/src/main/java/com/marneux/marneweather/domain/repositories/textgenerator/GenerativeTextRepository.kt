package com.marneux.marneweather.domain.repositories.textgenerator

import com.marneux.marneweather.model.weather.CurrentWeather

interface GenerativeTextRepository {
    suspend fun generateTextForWeatherDetails(
        weatherDetails: CurrentWeather
    ): Result<String>
}
