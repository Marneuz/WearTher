package com.marneux.marneweather.ui.views.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.R
import com.marneux.marneweather.model.location.LocationAutofillSuggestion
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import com.marneux.marneweather.model.weather.HourlyForecast
import com.marneux.marneweather.ui.views.home.composables.currentWeatherDetailCardItem
import com.marneux.marneweather.ui.views.home.composables.errorCardItem
import com.marneux.marneweather.ui.views.home.composables.savedLocationItems
import com.marneux.marneweather.ui.views.home.composables.searchBarItem
import com.marneux.marneweather.ui.views.home.composables.subHeaderItem

@Composable
fun HomeScreen(
    homeScreenUiState: HomeScreenUiState,
    snackbarHostState: SnackbarHostState,
    onSavedLocationDismissed: (BriefWeatherDetails) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSuggestionClick: (LocationAutofillSuggestion) -> Unit,
    onSavedLocationItemClick: (BriefWeatherDetails) -> Unit,
    onLocationPermissionGranted: () -> Unit,
    onRetryFetchingWeatherForSavedLocations: () -> Unit,
    modifier: Modifier = Modifier,
    onRetryFetchingWeatherForCurrentLocation: () -> Unit = onLocationPermissionGranted
) {
    HomeScreen(
        modifier = modifier,
        weatherDetailsOfSavedLocations = homeScreenUiState.weatherDetailsOfSavedLocations,
        suggestionsForSearchQuery = homeScreenUiState.autofillSuggestions,
        isSuggestionsListLoading = homeScreenUiState.isLoadingAutofillSuggestions,
        isCurrentWeatherDetailsLoading = homeScreenUiState.isLoadingWeatherDetailsOfCurrentLocation,
        isWeatherForSavedLocationsLoading = homeScreenUiState.isLoadingSavedLocations,
        weatherOfCurrentUserLocation = homeScreenUiState.weatherDetailsOfCurrentLocation,
        hourlyForecastsOfCurrentUserLocation = homeScreenUiState.hourlyForecastsForCurrentLocation,
        errorFetchingWeatherForCurrentLocation = homeScreenUiState.errorFetchingWeatherForCurrentLocation,
        errorFetchingWeatherForSavedLocations = homeScreenUiState.errorFetchingWeatherForSavedLocations,
        errorLoadingAutofillSuggestions = homeScreenUiState.errorFetchingAutofillSuggestions,
        onRetryFetchingWeatherForCurrentLocation = onRetryFetchingWeatherForCurrentLocation,
        onRetryFetchingWeatherForSavedLocations = onRetryFetchingWeatherForSavedLocations,
        onSavedLocationDismissed = onSavedLocationDismissed,
        onSearchQueryChange = onSearchQueryChange,
        onSuggestionClick = onSuggestionClick,
        onSavedLocationItemClick = onSavedLocationItemClick,
        onLocationPermissionGranted = onLocationPermissionGranted,
        snackbarHostState = snackbarHostState,
    )
}


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    weatherDetailsOfSavedLocations: List<BriefWeatherDetails>,
    suggestionsForSearchQuery: List<LocationAutofillSuggestion>,
    weatherOfCurrentUserLocation: BriefWeatherDetails?,
    hourlyForecastsOfCurrentUserLocation: List<HourlyForecast>?,
    isSuggestionsListLoading: Boolean = false,
    isWeatherForSavedLocationsLoading: Boolean = false,
    isCurrentWeatherDetailsLoading: Boolean,
    errorFetchingWeatherForCurrentLocation: Boolean,
    errorFetchingWeatherForSavedLocations: Boolean,
    errorLoadingAutofillSuggestions: Boolean,
    onRetryFetchingWeatherForSavedLocations: () -> Unit,
    onRetryFetchingWeatherForCurrentLocation: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSuggestionClick: (LocationAutofillSuggestion) -> Unit,
    onSavedLocationItemClick: (BriefWeatherDetails) -> Unit,
    onSavedLocationDismissed: (BriefWeatherDetails) -> Unit,
    onLocationPermissionGranted: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {

    val currentLocation = stringResource(id = R.string.current_location)
    val savedLocation = stringResource(id = R.string.saved_locations)
    val errorCurrentLoc = stringResource(id = R.string.error_current_loc)
    val errorSavedLoc = stringResource(id = R.string.error_saved_loc)

    var isSearchBarActive by remember { mutableStateOf(false) }
    var currentQueryText by remember { mutableStateOf("") }
    val clearQueryText = {
        currentQueryText = ""
        onSearchQueryChange("")
    }
    var shouldDisplayCurrentLocationWeatherSubHeader by remember { mutableStateOf(false) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { isPermitted ->
            val isCoarseLocationPermitted =
                isPermitted.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
            val isFineLocationPermitted =
                isPermitted.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
            if (isCoarseLocationPermitted || isFineLocationPermitted) {
                shouldDisplayCurrentLocationWeatherSubHeader = true
                onLocationPermissionGranted()
            }
        }
    )
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }
    Box {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
        ) {
            searchBarItem(
                currentSearchQuery = currentQueryText,
                onClearSearchQueryIconClick = clearQueryText,
                isSearchBarActive = isSearchBarActive,
                errorLoadingSuggestions = errorLoadingAutofillSuggestions,
                onSearchQueryChange = {
                    currentQueryText = it
                    onSearchQueryChange(it)
                },
                onSearchBarActiveChange = { isSearchBarActive = it },
                suggestionsForSearchQuery = suggestionsForSearchQuery,
                isSuggestionsListLoading = isSuggestionsListLoading,
                onSuggestionClick = onSuggestionClick
            )

            if (shouldDisplayCurrentLocationWeatherSubHeader) {
                subHeaderItem(
                    title = currentLocation,
                    isLoadingAnimationVisible = isCurrentWeatherDetailsLoading
                )
            }

            if (weatherOfCurrentUserLocation != null && hourlyForecastsOfCurrentUserLocation != null) {
                currentWeatherDetailCardItem(
                    weatherOfCurrentUserLocation = weatherOfCurrentUserLocation,
                    hourlyForecastsOfCurrentUserLocation = hourlyForecastsOfCurrentUserLocation,
                    onClick = { onSavedLocationItemClick(weatherOfCurrentUserLocation) }
                )
            }

            if (errorFetchingWeatherForCurrentLocation) {
                errorCardItem(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    errorMessage = errorCurrentLoc,
                    onRetryButtonClick = onRetryFetchingWeatherForCurrentLocation
                )
            }

            subHeaderItem(
                title = savedLocation,
                isLoadingAnimationVisible = isWeatherForSavedLocationsLoading
            )

            if (errorFetchingWeatherForSavedLocations) {
                errorCardItem(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    errorMessage = errorSavedLoc,
                    onRetryButtonClick = onRetryFetchingWeatherForSavedLocations
                )
            }

            savedLocationItems(
                savedLocationItemsList = weatherDetailsOfSavedLocations,
                onSavedLocationItemClick = onSavedLocationItemClick,
                onSavedLocationDismissed = onSavedLocationDismissed
            )
        }
        SnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            hostState = snackbarHostState
        )
    }
}







