package com.marneux.marneweather.presentation.views.weatherdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marneux.marneweather.domain.usecases.textgenerator.WeatherDetailsTextUseCase
import com.marneux.marneweather.domain.usecases.weather.HourlyForecastUseCase
import com.marneux.marneweather.domain.usecases.weather.PrecipitationProbabilityUseCase
import com.marneux.marneweather.domain.usecases.weather.SaveLocationUseCase
import com.marneux.marneweather.domain.usecases.weather.StreamSavedLocationsUseCase
import com.marneux.marneweather.domain.usecases.weather.TodayWeatherInfoUseCase
import com.marneux.marneweather.domain.usecases.weather.WeatherByLocationUseCase
import com.marneux.marneweather.presentation.navigation.NavigationDestinations
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val weatherDetailsTextUseCase: WeatherDetailsTextUseCase,
    private val weatherByLocationUseCase: WeatherByLocationUseCase,
    streamSavedLocationsUseCase: StreamSavedLocationsUseCase,
    private val saveLocationUseCase: SaveLocationUseCase,
    private val todayWeatherInfoUseCase:
    TodayWeatherInfoUseCase,
    private val hourlyForecastUseCase:
    HourlyForecastUseCase,
    private val precipitationProbabilityUseCase: PrecipitationProbabilityUseCase
) : ViewModel() {

    private val latitude: String =
        savedStateHandle[NavigationDestinations.WeatherDetailScreen.NAV_ARG_LATITUDE]
            ?: throw IllegalArgumentException("Latitude argument is missing")
    private val longitude: String =
        savedStateHandle[NavigationDestinations.WeatherDetailScreen.NAV_ARG_LONGITUDE]
            ?: throw IllegalArgumentException("Longitude argument is missing")
    private val nameLocation: String =
        savedStateHandle[NavigationDestinations.WeatherDetailScreen.NAV_ARG_NAME_OF_LOCATION]
            ?: throw IllegalArgumentException("Location name argument is missing")

    private val _uiState = MutableStateFlow(WeatherDetailScreenUiState())
    val uiState = _uiState as StateFlow<WeatherDetailScreenUiState>

    init {
        streamSavedLocationsUseCase.execute()
            .map { namesOfSavedLocationsList ->
                namesOfSavedLocationsList.any { it.nameLocation == nameLocation }
            }
            .onEach { isPreviouslySavedLocation ->
                _uiState.update { it.copy(isPreviouslySavedLocation = isPreviouslySavedLocation) }
            }
            .launchIn(scope = viewModelScope)

        viewModelScope.launch {
            try {
                fetchWeatherDetailsAndUpdateState()
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                _uiState.update { it.copy(isLoading = false, errorMessage = DEFAULT_ERROR_MESSAGE) }
            }
        }
    }

    private suspend fun fetchWeatherDetailsAndUpdateState(): Unit = coroutineScope {
        _uiState.update { it.copy(isLoading = true, isWeatherSummaryTextLoading = true) }

        val weatherDetailsChosenLocation = async {
            weatherByLocationUseCase.execute(
                nameLocation = nameLocation,
                latitude = latitude,
                longitude = longitude
            ).getOrThrow()
        }

        val summaryMessage = async {
            weatherDetailsTextUseCase.execute(
                weatherDetails = weatherDetailsChosenLocation.await()
            ).getOrNull()
        }

        val precipitationProbabilities =
            async {
                precipitationProbabilityUseCase.execute(
                    latitude = latitude,
                    longitude = longitude
                ).getOrThrow()
            }

        val hourlyForecasts = async {
            hourlyForecastUseCase.execute(
                latitude = latitude,
                longitude = longitude
            ).getOrThrow()
        }

        val additionalWeatherInfoItems = async {
            todayWeatherInfoUseCase.execute(
                latitude = latitude,
                longitude = longitude
            ).getOrThrow()
        }
        _uiState.update {
            it.copy(
                isLoading = false,
                weatherDetailsOfChosenLocation = weatherDetailsChosenLocation.await(),
                precipitationProbabilities = precipitationProbabilities.await(),
                hourlyForecasts = hourlyForecasts.await(),
                additionalWeatherInfoItems = additionalWeatherInfoItems.await()
            )
        }

        _uiState.update {
            it.copy(
                isWeatherSummaryTextLoading = false,
                weatherSummaryText = summaryMessage.await()
            )
        }
    }

    fun addLocationToSavedLocations() {
        viewModelScope.launch {
            saveLocationUseCase.execute(
                nameLocation = nameLocation,
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Default error message"
    }
}