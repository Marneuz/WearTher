package com.marneux.marneweather.ui.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marneux.marneweather.data.weather.mapper.toBriefWeatherDetails
import com.marneux.marneweather.domain.usecases.location.FetchSuggestedPlacesForQueryUseCase
import com.marneux.marneweather.domain.usecases.location.GetCurrentLocationUseCase
import com.marneux.marneweather.domain.usecases.location.GetLocationNameForCoordinatesUseCase
import com.marneux.marneweather.domain.usecases.weather.DeleteWeatherLocationFromSavedItemUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchHourlyForecastsForNext24HoursUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchWeatherForLocationUseCase
import com.marneux.marneweather.domain.usecases.weather.GetBriefWeatherDetailsUseCase
import com.marneux.marneweather.domain.usecases.weather.GetSavedLocationsListStreamUseCase
import com.marneux.marneweather.domain.usecases.weather.TryRestoringDeletedWeatherLocationUseCase
import com.marneux.marneweather.model.location.SavedLocation
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import com.marneux.marneweather.model.weather.CurrentWeather
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getBriefWeatherDetailsUseCase: GetBriefWeatherDetailsUseCase,
    private val getLocationNameForCoordinatesUseCase: GetLocationNameForCoordinatesUseCase,
    private val fetchWeatherForLocationUseCase: FetchWeatherForLocationUseCase,
    getSavedLocationsListStreamUseCase: GetSavedLocationsListStreamUseCase,
    private val deleteWeatherLocationFromSavedItemsUseCase:
    DeleteWeatherLocationFromSavedItemUseCase,
    private val tryRestoringDeletedWeatherLocationUseCase:
    TryRestoringDeletedWeatherLocationUseCase,
    private val fetchSuggestedPlacesForQueryUseCase: FetchSuggestedPlacesForQueryUseCase,
    private val fetchHourlyForecastsForNext24HoursUseCase:
    FetchHourlyForecastsForNext24HoursUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {

    private val currentSearchQuery = MutableStateFlow("")
    private val isCurrentlyRetryingToFetchSavedLocation = MutableStateFlow(false)
    private var recentlyDeletedItem: BriefWeatherDetails? = null
    private var currentWeather = mutableMapOf<SavedLocation, CurrentWeather>()

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState as StateFlow<HomeScreenUiState>

    init {
        combine(
            getSavedLocationsListStreamUseCase.execute(),
            isCurrentlyRetryingToFetchSavedLocation
        ) { savedLocations, _ ->
            savedLocations
        }.onEach {
            _uiState.update {
                it.copy(
                    isLoadingSavedLocations = true,
                    errorFetchingWeatherForSavedLocations = false
                )
            }
        }.map { savedLocations ->
            fetchCurrentWeatherDetails(savedLocations)
        }.onEach { weatherDetailsOfSavedLocationsResult ->
            val weatherDetailsOfSavedLocations =
                weatherDetailsOfSavedLocationsResult.getOrNull()
            _uiState.update {
                it.copy(
                    isLoadingSavedLocations = false,
                    weatherDetailsOfSavedLocations = weatherDetailsOfSavedLocations ?: emptyList(),
                    errorFetchingWeatherForSavedLocations = weatherDetailsOfSavedLocations == null
                )
            }
            isCurrentlyRetryingToFetchSavedLocation.update { false }
        }.launchIn(viewModelScope)

        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        currentSearchQuery.debounce(250)
            .distinctUntilChanged()
            .mapLatest { query ->
                if (query.isBlank()) return@mapLatest Result.success(emptyList())
                _uiState.update {
                    it.copy(
                        isLoadingAutofillSuggestions = true,
                        errorFetchingAutofillSuggestions = false
                    )
                }
                fetchSuggestedPlacesForQueryUseCase.execute(query)
            }
            .onEach { autofillSuggestionsResult ->
                val autofillSuggestions = autofillSuggestionsResult.getOrNull()
                _uiState.update {
                    it.copy(
                        isLoadingAutofillSuggestions = false,
                        autofillSuggestions = autofillSuggestions ?: emptyList(),
                        errorFetchingAutofillSuggestions = autofillSuggestions == null
                    )
                }
            }
            .launchIn(viewModelScope)

    }

    fun retryFetchingSavedLocations() {
        val isCurrentlyRetrying = isCurrentlyRetryingToFetchSavedLocation.value
        if (isCurrentlyRetrying) return
        isCurrentlyRetryingToFetchSavedLocation.update { true }
    }

    fun setSearchQueryForSuggestionsGeneration(searchQuery: String) {
        currentSearchQuery.value = searchQuery
    }

    fun deleteSavedWeatherLocation(briefWeatherDetails: BriefWeatherDetails) {
        recentlyDeletedItem = briefWeatherDetails
        viewModelScope.launch {
            deleteWeatherLocationFromSavedItemsUseCase.execute(briefWeatherDetails)
        }
    }

    fun restoreRecentlyDeletedItem() {
        recentlyDeletedItem?.let {
            viewModelScope.launch {
                tryRestoringDeletedWeatherLocationUseCase.execute(
                    it
                        .nameLocation
                )
            }
        }
    }

    private suspend fun fetchCurrentWeatherDetails(savedLocations: List<SavedLocation>): Result<List<BriefWeatherDetails>?> {
        val savedLocationsSet = savedLocations.toSet()
        val removedLocations = currentWeather.keys subtract savedLocationsSet
        for (removedLocation in removedLocations) {
            currentWeather.remove(removedLocation)
        }
        // fetch item si no esta en cache
        val locationsNotInCache = savedLocationsSet subtract currentWeather.keys
        for (savedLocationNotInCache in locationsNotInCache) {
            try {
                fetchWeatherForLocationUseCase.execute(
                    nameLocation = savedLocationNotInCache.nameLocation,
                    latitude = savedLocationNotInCache.coordinates.latitude,
                    longitude = savedLocationNotInCache.coordinates.longitude
                ).getOrThrow().also { currentWeather[savedLocationNotInCache] = it }
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                return Result.failure(exception)
            }
        }
        return Result.success(
            currentWeather.values.toList().map { it.toBriefWeatherDetails() })
    }

    fun fetchWeatherForCurrentUserLocation() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _uiState.update {
                it.copy(
                    isLoadingWeatherDetailsOfCurrentLocation = false,
                    errorFetchingWeatherForCurrentLocation = true
                )
            }
        }
        viewModelScope.launch(exceptionHandler) {

            _uiState.update {
                it.copy(
                    isLoadingWeatherDetailsOfCurrentLocation = true,
                    errorFetchingWeatherForCurrentLocation = false
                )
            }

//            val coordinates = currentLocationRepository.getCurrentLocation().getOrThrow()
            val coordinates = getCurrentLocationUseCase.execute().getOrThrow()
            val nameLocation = getLocationNameForCoordinatesUseCase.execute(
                coordinates.latitude.toDouble(),
                coordinates.longitude.toDouble()
            ).getOrThrow()

            val weatherDetailsForCurrentLocation = async {
                fetchWeatherForLocationUseCase.execute(
                    nameLocation = nameLocation,
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude
                ).getOrThrow().toBriefWeatherDetails()
            }

            val hourlyForecastsForCurrentLocation = async {
                fetchHourlyForecastsForNext24HoursUseCase.execute(
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude
                ).getOrThrow()
            }
            _uiState.update {
                it.copy(
                    isLoadingWeatherDetailsOfCurrentLocation = false,
                    errorFetchingWeatherForCurrentLocation = false,
                    weatherDetailsOfCurrentLocation = weatherDetailsForCurrentLocation.await(),
                    hourlyForecastsForCurrentLocation = hourlyForecastsForCurrentLocation.await(),
                )
            }
        }
    }
}