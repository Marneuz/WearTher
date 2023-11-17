package com.marneux.marneweather.presentation.views.weatherdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marneux.marneweather.domain.usecases.location.ListSavedLocationUseCase
import com.marneux.marneweather.domain.usecases.location.SaveLocationUseCase
import com.marneux.marneweather.domain.usecases.textgenerator.WeatherDetailsTextUseCase
import com.marneux.marneweather.domain.usecases.weather.HourlyForecastUseCase
import com.marneux.marneweather.domain.usecases.weather.RainChancesUseCase
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
    listSavedLocationUseCase: ListSavedLocationUseCase,
    private val saveLocationUseCase: SaveLocationUseCase,
    private val todayWeatherInfoUseCase:
    TodayWeatherInfoUseCase,
    private val hourlyForecastUseCase:
    HourlyForecastUseCase,
    private val rainChancesUseCase: RainChancesUseCase
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

    private val _uiState = MutableStateFlow(WeatherDetailState())
    val uiState = _uiState as StateFlow<WeatherDetailState>

    // Inicialización del ViewModel. Aquí se manejan los argumentos pasados y se inicializan los flujos de datos.
    init {
        // Este flujo verifica si la ubicación actual ya está guardada y actualiza el estado de la UI.
        listSavedLocationUseCase.execute()
            .map { namesOfSavedLocationsList ->
                namesOfSavedLocationsList.any { it.nameLocation == nameLocation }
            }
            .onEach { isPreviouslySavedLocation ->
                _uiState.update { it.copy(isPreviouslySavedLocation = isPreviouslySavedLocation) }
            }
            .launchIn(scope = viewModelScope)

        // Lanza una corrutina para obtener los detalles del clima y actualizar el estado de la UI.
        viewModelScope.launch {
            try {
                fetchWeatherDetailsAndUpdateState()
            } catch (exception: Exception) {
                // Manejo de excepciones en la corrutina, actualizando el estado de error en la UI.
                if (exception is CancellationException) throw exception
                _uiState.update { it.copy(isLoading = false, errorMessage = DEFAULT_ERROR_MESSAGE) }
            }
        }
    }

    // Función para obtener y actualizar los detalles del clima.
    private suspend fun fetchWeatherDetailsAndUpdateState(): Unit = coroutineScope {
        _uiState.update { it.copy(isLoading = true, isWeatherSummaryTextLoading = true) }

        // Realiza varias operaciones asíncronas en paralelo para obtener los datos del clima.
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
                rainChancesUseCase.execute(
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
        // Actualiza el estado de la UI con los datos obtenidos.
        _uiState.update {
            it.copy(
                isLoading = false,
                weatherDetailsOfChosenLocation = weatherDetailsChosenLocation.await(),
                precipitationProbabilities = precipitationProbabilities.await(),
                hourlyForecasts = hourlyForecasts.await(),
                additionalWeatherInfoItems = additionalWeatherInfoItems.await()
            )
        }
        // Actualiza el estado de la UI con la respuesta de gpt del clima.
        _uiState.update {
            it.copy(
                isWeatherSummaryTextLoading = false,
                weatherSummaryText = summaryMessage.await()
            )
        }
    }

    // Función para agregar la ubicación actual a las ubicaciones guardadas.
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