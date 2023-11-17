package com.marneux.marneweather.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marneux.marneweather.model.location.AutoSuggestLocation
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import com.marneux.marneweather.presentation.views.home.HomeScreen
import com.marneux.marneweather.presentation.views.home.HomeViewModel
import com.marneux.marneweather.presentation.views.weatherdetail.WeatherDetailScreen
import com.marneux.marneweather.presentation.views.weatherdetail.WeatherDetailViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(navController: NavHostController = rememberNavController()) {
    // Crea un host de navegación para manejar las pantallas de la aplicación.
    NavHost(
        navController = navController,
        startDestination = NavigationDestinations.HomeScreen.route
    ) {
        // Define la pantalla de inicio y la pantalla de detalle del clima en la navegación.
        homeScreen(
            route = NavigationDestinations.HomeScreen.route,
            onSuggestionClick = {
                navController.navigateToWeatherDetailScreen(
                    nameLocation = it.nameLocation,
                    latitude = it.coordinatesLocation.latitude,
                    longitude = it.coordinatesLocation.longitude
                )
            },
            onSavedLocationItemClick = {
                navController.navigateToWeatherDetailScreen(
                    nameLocation = it.nameLocation,
                    latitude = it.coordinates.latitude,
                    longitude = it.coordinates.longitude
                )
            }
        )
        weatherDetailScreen(
            route = NavigationDestinations.WeatherDetailScreen.route,
            onBackButtonClick = navController::popBackStack
        )
    }
}

private fun NavGraphBuilder.homeScreen(
    route: String,
    onSuggestionClick: (suggestion: AutoSuggestLocation) -> Unit,
    onSavedLocationItemClick: (BriefWeatherDetails) -> Unit
) {
    composable(route = route) {
        // Inicialización del ViewModel de la pantalla de inicio y manejo de su estado.
        val viewModel: HomeViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsState()

        // Configuración de componentes reactivos y manejo de eventos de Snackbar.
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val showSnackbar = { briefWeatherDetails: BriefWeatherDetails ->
            coroutineScope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = "${briefWeatherDetails.nameLocation} has been deleted",
                    actionLabel = "Undo",
                    duration = SnackbarDuration.Short
                )
                if (snackbarResult == SnackbarResult.ActionPerformed) {
                    viewModel.restoreDeletedSavedLocation()
                }
            }
        }
        // Pantalla de inicio con su estado y manejadores de eventos.
        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onSavedLocationDismissed = {
                viewModel.deleteSavedLocation(it)
                showSnackbar(it)
            },
            onSearchQueryChange = viewModel::setSearchQueryForSuggestionsGeneration,
            onSuggestionClick = onSuggestionClick,
            onSavedLocationItemClick = onSavedLocationItemClick,
            onLocationPermissionGranted = viewModel::fetchWeatherCurrentLocation,
            onRetryFetchWeatherSavedLocations = viewModel::retryFetchingSavedLocations
        )
    }
}

fun NavGraphBuilder.weatherDetailScreen(
    route: String,
    onBackButtonClick: () -> Unit
) {
    composable(route) {
        // Inicialización del ViewModel de la pantalla de detalle del clima y manejo de su estado.
        val viewModel: WeatherDetailViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        // Configuración de componentes reactivos y manejo de eventos de Snackbar.
        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        WeatherDetailScreen(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onBackButtonClick = onBackButtonClick,
            onSaveButtonClick = {
                viewModel.addLocationToSavedLocations()
                snackbarHostState.currentSnackbarData?.dismiss()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message = "Added to saved locations")
                }
            }
        )
    }
}

private fun NavHostController.navigateToWeatherDetailScreen(
    nameLocation: String,
    latitude: String,
    longitude: String
) {
    // Función auxiliar para navegar a la pantalla de detalle del clima.
    val destination = NavigationDestinations.WeatherDetailScreen.buildRoute(
        nameLocation = nameLocation,
        latitude = latitude,
        longitude = longitude
    )
    navigate(destination) { launchSingleTop = true }
}
