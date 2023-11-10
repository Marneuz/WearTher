package com.marneux.marneweather.ui.views.weatherdetail

import com.marneux.marneweather.model.weather.CurrentWeather
import com.marneux.marneweather.model.weather.HourlyForecast
import com.marneux.marneweather.model.weather.PrecipitationProbability
import com.marneux.marneweather.model.weather.SingleWeatherDetail

data class WeatherDetailScreenUiState(
    val isLoading: Boolean = true,
    val isPreviouslySavedLocation: Boolean = false,
    val weatherDetailsOfChosenLocation: CurrentWeather? = null,
    val isWeatherSummaryTextLoading: Boolean = false,
    val weatherSummaryText: String? = null,
    val errorMessage: String? = null,
    val precipitationProbabilities: List<PrecipitationProbability> = emptyList(),
    val hourlyForecasts: List<HourlyForecast> = emptyList(),
    val additionalWeatherInfoItems: List<SingleWeatherDetail> = emptyList()
)