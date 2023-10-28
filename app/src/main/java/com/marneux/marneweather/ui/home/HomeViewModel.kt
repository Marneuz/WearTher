package com.marneux.marneweather.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marneux.marneweather.data.remote.location.ReverseGeocoder
import com.marneux.marneweather.data.repositories.location.LocationServicesRepositoryImpl
import com.marneux.marneweather.data.repositories.weather.WeatherRepositoryImpl
import com.marneux.marneweather.data.repositories.weather.fetchHourlyForecastsForNext24Hours
import com.marneux.marneweather.domain.location.CurrentLocationProviderImpl
import com.marneux.marneweather.domain.models.location.SavedLocation
import com.marneux.marneweather.domain.models.weather.BriefWeatherDetails
import com.marneux.marneweather.domain.models.weather.CurrentWeatherDetails
import com.marneux.marneweather.domain.models.weather.toBriefWeatherDetails
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
    private val currentLocationProvider: CurrentLocationProviderImpl,
    private val reverseGeocoder: ReverseGeocoder,
    private val locationServicesRepository: LocationServicesRepositoryImpl,
    private val weatherRepository: WeatherRepositoryImpl
) : ViewModel() {

    private val currentSearchQuery = MutableStateFlow("")
    private val isCurrentlyRetryingToFetchSavedLocation = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState as StateFlow<HomeScreenUiState>


    private var currentWeatherDetailsCache = mutableMapOf<SavedLocation, CurrentWeatherDetails>()
    private var recentlyDeletedItem: BriefWeatherDetails? = null

    init {

        combine(
            weatherRepository.getSavedLocationsListStream(),
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
                locationServicesRepository.fetchSuggestedPlacesForQuery(query)
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

    /**
     * Used to set the [searchQuery] for which the suggestions should be generated.
     */
    fun setSearchQueryForSuggestionsGeneration(searchQuery: String) {
        currentSearchQuery.value = searchQuery
    }

    fun deleteSavedWeatherLocation(briefWeatherDetails: BriefWeatherDetails) {
        recentlyDeletedItem = briefWeatherDetails
        viewModelScope.launch {
            weatherRepository.deleteWeatherLocationFromSavedItems(briefWeatherDetails)
        }
    }

    fun restoreRecentlyDeletedItem() {
        recentlyDeletedItem?.let {
            viewModelScope.launch {
                weatherRepository.tryRestoringDeletedWeatherLocation(
                    it
                        .nameLocation
                )
            }
        }
    }


    private suspend fun fetchCurrentWeatherDetailsWithCache(savedLocations: List<SavedLocation>): Result<List<BriefWeatherDetails>?> {
        val savedLocationsSet = savedLocations.toSet()
        val removedLocations = currentWeatherDetailsCache.keys subtract savedLocationsSet
        for (removedLocation in removedLocations) {
            currentWeatherDetailsCache.remove(removedLocation)
        }
        // only fetch weather details of the items that are not in cache.
        val locationsNotInCache = savedLocationsSet subtract currentWeatherDetailsCache.keys
        for (savedLocationNotInCache in locationsNotInCache) {
            try {
                weatherRepository.fetchWeatherForLocation(
                    nameLocation = savedLocationNotInCache.nameLocation,
                    latitude = savedLocationNotInCache.coordinates.latitude,
                    longitude = savedLocationNotInCache.coordinates.longitude
                ).getOrThrow().also { currentWeatherDetailsCache[savedLocationNotInCache] = it }
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                return Result.failure(exception)
            }
        }
        return Result.success(
            currentWeatherDetailsCache.values.toList().map { it.toBriefWeatherDetails() })
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
                weatherRepository.fetchWeatherForLocation(
                    nameLocation = nameLocation,
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude
                ).getOrThrow().toBriefWeatherDetails()
            }

            val hourlyForecastsForCurrentLocation = async {
                weatherRepository.fetchHourlyForecastsForNext24Hours(
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