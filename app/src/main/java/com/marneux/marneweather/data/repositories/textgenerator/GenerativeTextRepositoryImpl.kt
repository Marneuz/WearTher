package com.marneux.marneweather.data.repositories.textgenerator

import com.marneux.marneweather.domain.models.weather.CurrentWeatherDetails

interface GenerativeTextRepositoryImpl {


    suspend fun generateTextForWeatherDetails(weatherDetails: CurrentWeatherDetails): Result<String>
}
