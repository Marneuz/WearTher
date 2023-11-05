package com.marneux.marneweather.domain.repositories.textgenerator

import com.marneux.marneweather.domain.cajondesastre.location.models.weather.CurrentWeather

interface GenerativeTextRepository {
    suspend fun generateTextForWeatherDetails(
        weatherDetails: CurrentWeather
    ): Result<String>
}
