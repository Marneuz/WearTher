package com.marneux.marneweather.ui.weatherdetail

import com.marneux.marneweather.domain.cajondesastre.location.models.weather.CurrentWeather
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.HourlyForecast
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.PrecipitationProbability
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.SingleWeatherDetail

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