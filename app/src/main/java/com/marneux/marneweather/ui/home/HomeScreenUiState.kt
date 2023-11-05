package com.marneux.marneweather.ui.home

import com.marneux.marneweather.domain.cajondesastre.location.models.location.LocationAutofillSuggestion
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.BriefWeatherDetails
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.HourlyForecast

data class HomeScreenUiState(
    val isLoadingAutofillSuggestions: Boolean = false,
    val isLoadingSavedLocations: Boolean = false,
    val isLoadingWeatherDetailsOfCurrentLocation: Boolean = false,
    val errorFetchingWeatherForCurrentLocation: Boolean = false,
    val errorFetchingWeatherForSavedLocations: Boolean = false,
    val errorFetchingAutofillSuggestions: Boolean = false,
    val weatherDetailsOfCurrentLocation: BriefWeatherDetails? = null,
    val hourlyForecastsForCurrentLocation: List<HourlyForecast>? = null,
    val autofillSuggestions: List<LocationAutofillSuggestion> = emptyList(),
    val weatherDetailsOfSavedLocations: List<BriefWeatherDetails> = emptyList(),
)