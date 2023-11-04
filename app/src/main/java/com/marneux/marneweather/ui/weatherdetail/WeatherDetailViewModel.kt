package com.marneux.marneweather.ui.weatherdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marneux.marneweather.data.repositories.textgenerator.GenerativeTextRepositoryImpl
import com.marneux.marneweather.data.repositories.weather.WeatherRepositoryImpl
import com.marneux.marneweather.data.repositories.weather.fetchHourlyForecastsForNext24Hours
import com.marneux.marneweather.data.repositories.weather.fetchPrecipitationProbabilitiesForNext24hours
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
    private val weatherRepository: WeatherRepositoryImpl,
    private val generativeTextRepository: GenerativeTextRepositoryImpl
) : ViewModel() {


    private val latitude: String =
        savedStateHandle[NavigationDestinations.WeatherDetailScreen.NAV_ARG_LATITUDE]!!
    private val longitude: String =
        savedStateHandle[NavigationDestinations.WeatherDetailScreen.NAV_ARG_LONGITUDE]!!
    private val nameLocation: String =
        savedStateHandle[NavigationDestinations.WeatherDetailScreen.NAV_ARG_NAME_OF_LOCATION]!!


    private val _uiState = MutableStateFlow(WeatherDetailScreenUiState())
    val uiState = _uiState as StateFlow<WeatherDetailScreenUiState>

    init {
        weatherRepository.getSavedLocationsListStream()
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
            weatherRepository.fetchWeatherForLocation(
                nameLocation = nameLocation,
                latitude = latitude,
                longitude = longitude
            ).getOrThrow()
        }

        val summaryMessage = async {
            generativeTextRepository.generateTextForWeatherDetails(
                weatherDetails = weatherDetailsOfChosenLocation.await()
            ).getOrNull()
        }

        val precipitationProbabilities =
            async {
                weatherRepository.fetchPrecipitationProbabilitiesForNext24hours(
                    latitude = latitude,
                    longitude = longitude
                ).getOrThrow()
            }

        val hourlyForecasts = async {
            weatherRepository.fetchHourlyForecastsForNext24Hours(
                latitude = latitude,
                longitude = longitude
            ).getOrThrow()
        }

        val additionalWeatherInfoItems = async {
            weatherRepository.fetchAdditionalWeatherInfoItemsListForCurrentDay(
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
            weatherRepository.saveWeatherLocation(
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