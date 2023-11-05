package com.marneux.marneweather.ui.navigation

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
import com.marneux.marneweather.domain.cajondesastre.location.models.location.LocationAutofillSuggestion
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.BriefWeatherDetails
import com.marneux.marneweather.ui.home.HomeScreen
import com.marneux.marneweather.ui.home.HomeViewModel
import com.marneux.marneweather.ui.weatherdetail.WeatherDetailScreen
import com.marneux.marneweather.ui.weatherdetail.WeatherDetailViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun Navigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = NavigationDestinations.HomeScreen.route
    ) {

        homeScreen(
            route = NavigationDestinations.HomeScreen.route,
            onSuggestionClick = {
                navController.navigateToWeatherDetailScreen(
                    nameOfLocation = it.nameLocation,
                    latitude = it.coordinatesLocation.latitude,
                    longitude = it.coordinatesLocation.longitude
                )
            },
            onSavedLocationItemClick = {
                navController.navigateToWeatherDetailScreen(
                    nameOfLocation = it.nameLocation,
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
    onSuggestionClick: (suggestion: LocationAutofillSuggestion) -> Unit,
    onSavedLocationItemClick: (BriefWeatherDetails) -> Unit
) {
    composable(route = route) {
        val viewModel: HomeViewModel = koinViewModel()

        val uiState by viewModel.uiState.collectAsState()
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
                    viewModel.restoreRecentlyDeletedItem()
                }
            }
        }

        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            homeScreenUiState = uiState,
            snackbarHostState = snackbarHostState,
            onSavedLocationDismissed = {
                viewModel.deleteSavedWeatherLocation(it)
                showSnackbar(it)
            },
            onSearchQueryChange = viewModel::setSearchQueryForSuggestionsGeneration,
            onSuggestionClick = onSuggestionClick,
            onSavedLocationItemClick = onSavedLocationItemClick,
            onLocationPermissionGranted = viewModel::fetchWeatherForCurrentUserLocation,
            onRetryFetchingWeatherForSavedLocations = viewModel::retryFetchingSavedLocations
        )
    }
}

fun NavGraphBuilder.weatherDetailScreen(
    route: String,
    onBackButtonClick: () -> Unit
) {


    composable(route) {
        // val viewModel = hiltViewModel<WeatherDetailViewModel>()
        val viewModel: WeatherDetailViewModel = koinViewModel()

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
    nameOfLocation: String,
    latitude: String,
    longitude: String
) {
    val destination = NavigationDestinations.WeatherDetailScreen.buildRoute(
        nameLocation = nameOfLocation,
        latitude = latitude,
        longitude = longitude
    )
    navigate(destination) { launchSingleTop = true }
}
