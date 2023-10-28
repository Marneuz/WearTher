package com.marneux.marneweather.ui.weatherdetail


import com.marneux.marneweather.domain.models.weather.CurrentWeatherDetails
import com.marneux.marneweather.domain.models.weather.HourlyForecast
import com.marneux.marneweather.domain.models.weather.PrecipitationProbability
import com.marneux.marneweather.domain.models.weather.SingleWeatherDetail

/**
 * A UI state class that represents the current UI state of the [WeatherDetailScreen].
 */
data class WeatherDetailScreenUiState(
    val isLoading: Boolean = true,
    val isPreviouslySavedLocation: Boolean = false,
    val weatherDetailsOfChosenLocation: CurrentWeatherDetails? = null,
    val isWeatherSummaryTextLoading: Boolean = false,
    val weatherSummaryText: String? = null,
    val errorMessage: String? = null,
    val precipitationProbabilities: List<PrecipitationProbability> = emptyList(),
    val hourlyForecasts: List<HourlyForecast> = emptyList(),
    val additionalWeatherInfoItems: List<SingleWeatherDetail> = emptyList()
)