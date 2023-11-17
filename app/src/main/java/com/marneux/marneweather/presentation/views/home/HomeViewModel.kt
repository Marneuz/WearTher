package com.marneux.marneweather.presentation.views.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marneux.marneweather.domain.usecases.location.CurrentLocationUseCase
import com.marneux.marneweather.domain.usecases.location.DeleteSavedLocationUseCase
import com.marneux.marneweather.domain.usecases.location.ListSavedLocationUseCase
import com.marneux.marneweather.domain.usecases.location.LocationNameCoordsUseCase
import com.marneux.marneweather.domain.usecases.location.RestoreLocationUseCase
import com.marneux.marneweather.domain.usecases.location.SuggestedLocationUseCase
import com.marneux.marneweather.domain.usecases.weather.BriefWeatherDetailsUseCase
import com.marneux.marneweather.domain.usecases.weather.HourlyForecastUseCase
import com.marneux.marneweather.domain.usecases.weather.WeatherByLocationUseCase
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
    listSavedLocationUseCase: ListSavedLocationUseCase,
    private val briefWeatherDetailsUseCase: BriefWeatherDetailsUseCase,
    private val locationNameCoordsUseCase: LocationNameCoordsUseCase,
    private val weatherByLocationUseCase: WeatherByLocationUseCase,
    private val deleteSavedLocationUseCase: DeleteSavedLocationUseCase,
    private val restoreLocationUseCase: RestoreLocationUseCase,
    private val suggestedLocationUseCase: SuggestedLocationUseCase,
    private val hourlyForecastUseCase: HourlyForecastUseCase,
    private val currentLocationUseCase: CurrentLocationUseCase
) : ViewModel() {


    var isRefreshing by mutableStateOf(false)
        private set

    private val currentSearchQuery = MutableStateFlow("")
    private val retryFetchSavedLocation = MutableStateFlow(false)
    private var recentlyDeletedItem: BriefWeatherDetails? = null
    private var currentWeather = mutableMapOf<SavedLocation, CurrentWeather>()

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState as StateFlow<HomeState>

    /** Esta función combina dos flujos de datos: las ubicaciones guardadas y un disparador de
    reintento. Se actualiza el estado de la UI cada vez que cambia alguno de estos flujos.*/
    init {
        combine(
            listSavedLocationUseCase.execute(),
            retryFetchSavedLocation
        ) { savedLocations, _ ->
            savedLocations
        }.onEach {
            // Actualiza el estado de la UI antes de comenzar a cargar nuevos datos.
            _uiState.update {
                it.copy(
                    isLoadingSavedLocations = true,
                    errorFetchWeatherSavedLocations = false
                )
            }
        }.map { savedLocations ->
            // Procesa las ubicaciones guardadas y obtiene los detalles del clima.
            fetchCurrentWeatherDetails(savedLocations)
        }.onEach { weatherSavedLocationsResult ->
            // Actualiza el estado de la UI con los resultados obtenidos.
            val weatherSavedLocations =
                weatherSavedLocationsResult.getOrNull()
            _uiState.update {
                it.copy(
                    isLoadingSavedLocations = false,
                    weatherSavedLocations = weatherSavedLocations ?: emptyList(),
                    errorFetchWeatherSavedLocations = weatherSavedLocations == null
                )
            }
            retryFetchSavedLocation.update { false }
        }.launchIn(viewModelScope)

        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        currentSearchQuery.debounce(250)
            .distinctUntilChanged()
            .mapLatest { query ->
                if (query.isBlank()) return@mapLatest Result.success(emptyList())
                _uiState.update {
                    it.copy(
                        isLoadingAutofillSuggestions = true,
                        errorFetchAutofillSuggestions = false
                    )
                }
                suggestedLocationUseCase.execute(query)
            }
            .onEach { autofillSuggestionsResult ->
                val autofillSuggestions = autofillSuggestionsResult.getOrNull()
                _uiState.update {
                    it.copy(
                        isLoadingAutofillSuggestions = false,
                        autofillSuggestions = autofillSuggestions ?: emptyList(),
                        errorFetchAutofillSuggestions = autofillSuggestions == null
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun refreshScreen() {
        isRefreshing = true
        fetchWeatherCurrentLocation()
        isRefreshing = false
    }

    fun retryFetchingSavedLocations() {
        val isCurrentlyRetrying = retryFetchSavedLocation.value
        if (isCurrentlyRetrying) return
        retryFetchSavedLocation.update { true }
    }

    fun setSearchQueryForSuggestionsGeneration(searchQuery: String) {
        currentSearchQuery.value = searchQuery
    }

    fun deleteSavedLocation(briefWeatherDetails: BriefWeatherDetails) {
        recentlyDeletedItem = briefWeatherDetails
        viewModelScope.launch {
            deleteSavedLocationUseCase.execute(briefWeatherDetails)
        }
    }

    fun restoreDeletedSavedLocation() {
        recentlyDeletedItem?.let {
            viewModelScope.launch {
                restoreLocationUseCase.execute(
                    it.nameLocation
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
        val locationsNotSaved = savedLocationsSet subtract currentWeather.keys
        // Itera sobre las ubicaciones guardadas y obtiene los detalles del clima.
        for (savedLocationNotStored in locationsNotSaved) {
            try {
                // Ejecuta la consulta de clima y maneja excepciones.
                weatherByLocationUseCase.execute(
                    nameLocation = savedLocationNotStored.nameLocation,
                    latitude = savedLocationNotStored.coordinates.latitude,
                    longitude = savedLocationNotStored.coordinates.longitude
                ).getOrThrow().also { currentWeather[savedLocationNotStored] = it }
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                return Result.failure(exception)
            }
        }
        // Devuelve los detalles del clima obtenidos.
        return Result.success(
            currentWeather.values.toList().map { currentWeather ->
                briefWeatherDetailsUseCase(currentWeather)
            }
        )
    }

    fun fetchWeatherCurrentLocation() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _uiState.update {
                it.copy(
                    isLoadingWeatherCurrentLocation = false,
                    errorFetchWeatherCurrentLocation = true
                )
            }
        }
        viewModelScope.launch(exceptionHandler) {
            _uiState.update {
                it.copy(
                    isLoadingWeatherCurrentLocation = true,
                    errorFetchWeatherCurrentLocation = false
                )
            }

            val coordinates = currentLocationUseCase.execute().getOrThrow()
            val nameLocation = locationNameCoordsUseCase.execute(
                coordinates.latitude.toDouble(),
                coordinates.longitude.toDouble()
            ).getOrThrow()

            val weatherDetailsCurrentLocation = async {
                val currentWeather = weatherByLocationUseCase.execute(
                    nameLocation = nameLocation,
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude
                ).getOrThrow()
                briefWeatherDetailsUseCase(currentWeather)
            }

            val hourlyForecastsCurrentLocation = async {
                hourlyForecastUseCase.execute(
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude
                ).getOrThrow()
            }
            _uiState.update {
                it.copy(
                    isLoadingWeatherCurrentLocation = false,
                    errorFetchWeatherCurrentLocation = false,
                    weatherDetailsCurrentLocation = weatherDetailsCurrentLocation.await(),
                    hourlyForecastsCurrentLocation = hourlyForecastsCurrentLocation.await(),
                )
            }
        }
    }
}