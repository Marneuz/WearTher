package com.marneux.marneweather.ui.views.weatherdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marneux.marneweather.domain.usecases.textgenerator.GenerateTextForWeatherDetailsUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchAdditionalWeatherInfoItemsListForCurrentDayUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchHourlyForecastsForNext24HoursUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchPrecipitationProbabilitiesForNext24hoursUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchWeatherForLocationUseCase
import com.marneux.marneweather.domain.usecases.weather.GetSavedLocationsListStreamUseCase
import com.marneux.marneweather.domain.usecases.weather.SaveWeatherLocationUseCase
import com.marneux.marneweather.ui.navigation.NavigationDestinations
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
    private val generateTextForWeatherDetailsUseCase: GenerateTextForWeatherDetailsUseCase,
    private val fetchWeatherForLocationUseCase: FetchWeatherForLocationUseCase,
    getSavedLocationsListStreamUseCase: GetSavedLocationsListStreamUseCase,
    private val saveWeatherLocationUseCase: SaveWeatherLocationUseCase,
    private val fetchAdditionalWeatherInfoItemsListForCurrentDayUseCase:
    FetchAdditionalWeatherInfoItemsListForCurrentDayUseCase,
    private val fetchHourlyForecastsForNext24HoursUseCase:
    FetchHourlyForecastsForNext24HoursUseCase,
    private val fetchPrecipitationProbabilitiesForNext24hoursUseCase: FetchPrecipitationProbabilitiesForNext24hoursUseCase
) : ViewModel() {

    /**
     * Se que no es buena practica la doble exclamacion para asegurar que un dato no es null, pero
     * por limpieza del codigo y dado que la navegacion esta controlada y estos datos siempre tendran
     * contenido, me he permitido la licencia para evitar boilerplate.
     * */
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
        getSavedLocationsListStreamUseCase.execute()
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

        val weatherDetailsOfChosenLocation = async {
            fetchWeatherForLocationUseCase.execute(
                nameLocation = nameLocation,
                latitude = latitude,
                longitude = longitude
            ).getOrThrow()
        }

        val summaryMessage = async {
            generateTextForWeatherDetailsUseCase.execute(
                weatherDetails = weatherDetailsOfChosenLocation.await()
            ).getOrNull()
        }

        val precipitationProbabilities =
            async {
                fetchPrecipitationProbabilitiesForNext24hoursUseCase.execute(
                    latitude = latitude,
                    longitude = longitude
                ).getOrThrow()
            }

        val hourlyForecasts = async {
            fetchHourlyForecastsForNext24HoursUseCase.execute(
                latitude = latitude,
                longitude = longitude
            ).getOrThrow()
        }

        val additionalWeatherInfoItems = async {
            fetchAdditionalWeatherInfoItemsListForCurrentDayUseCase.execute(
                latitude = latitude,
                longitude = longitude
            ).getOrThrow()
        }
        _uiState.update {
            it.copy(
                isLoading = false,
                weatherDetailsOfChosenLocation = weatherDetailsOfChosenLocation.await(),
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
            saveWeatherLocationUseCase.execute(
                nameLocation = nameLocation,
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "errorDefaultString"
    }
}