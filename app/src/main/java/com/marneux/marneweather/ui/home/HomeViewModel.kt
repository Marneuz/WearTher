package com.marneux.marneweather.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marneux.marneweather.data.location.remote.ReverseGeocoder
import com.marneux.marneweather.data.weather.remote.mapper.toBriefWeatherDetails
import com.marneux.marneweather.domain.cajondesastre.location.models.location.SavedLocation
import com.marneux.marneweather.domain.cajondesastre.location.models.locationprovider.CurrentLocationProvider
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.BriefWeatherDetails
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.CurrentWeather
import com.marneux.marneweather.domain.repositories.location.LocationServicesRepository
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import com.marneux.marneweather.domain.usecases.location.FetchSuggestedPlacesForQueryUseCase
import com.marneux.marneweather.domain.usecases.weather.DeleteWeatherLocationFromSavedItemUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchHourlyForecastsForNext24HoursUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchWeatherForLocationUseCase
import com.marneux.marneweather.domain.usecases.weather.GetSavedLocationsListStreamUseCase
import com.marneux.marneweather.domain.usecases.weather.TryRestoringDeletedWeatherLocationUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val currentLocationProvider: CurrentLocationProvider,
    private val reverseGeocoder: ReverseGeocoder,
    private val locationServicesRepository: LocationServicesRepository,
    private val weatherRepository: WeatherRepository,//los dejo para que puto jalper vea que ya
    // no se usan
    private val fetchWeatherForLocationUseCase: FetchWeatherForLocationUseCase,
    getSavedLocationsListStreamUseCase: GetSavedLocationsListStreamUseCase,
    private val deleteWeatherLocationFromSavedItemsUseCase:
    DeleteWeatherLocationFromSavedItemUseCase,
    private val tryRestoringDeletedWeatherLocationUseCase:
    TryRestoringDeletedWeatherLocationUseCase,
    private val fetchSuggestedPlacesForQueryUseCase: FetchSuggestedPlacesForQueryUseCase,
    private val fetchHourlyForecastsForNext24HoursUseCase:
    FetchHourlyForecastsForNext24HoursUseCase
) : ViewModel() {


    private val currentSearchQuery = MutableStateFlow("")
    private val isCurrentlyRetryingToFetchSavedLocation = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState as StateFlow<HomeScreenUiState>


    private var currentWeatherCache = mutableMapOf<SavedLocation, CurrentWeather>()
    private var recentlyDeletedItem: BriefWeatherDetails? = null

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

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
            fetchCurrentWeatherDetailsWithCache(savedLocations)
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
        _isReady.value = true

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


    private suspend fun fetchCurrentWeatherDetailsWithCache(savedLocations: List<SavedLocation>): Result<List<BriefWeatherDetails>?> {
        val savedLocationsSet = savedLocations.toSet()
        val removedLocations = currentWeatherCache.keys subtract savedLocationsSet
        for (removedLocation in removedLocations) {
            currentWeatherCache.remove(removedLocation)
        }
        // fetch item si no esta en cache
        val locationsNotInCache = savedLocationsSet subtract currentWeatherCache.keys
        for (savedLocationNotInCache in locationsNotInCache) {
            try {
                fetchWeatherForLocationUseCase.execute(
                    nameLocation = savedLocationNotInCache.nameLocation,
                    latitude = savedLocationNotInCache.coordinates.latitude,
                    longitude = savedLocationNotInCache.coordinates.longitude
                ).getOrThrow().also { currentWeatherCache[savedLocationNotInCache] = it }
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                return Result.failure(exception)
            }
        }
        return Result.success(
            currentWeatherCache.values.toList().map { it.toBriefWeatherDetails() })
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

            val coordinates = currentLocationProvider.getCurrentLocation().getOrThrow()
            val nameLocation = reverseGeocoder.getLocationNameForCoordinates(
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