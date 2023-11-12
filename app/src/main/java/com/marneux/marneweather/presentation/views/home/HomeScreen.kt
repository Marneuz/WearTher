package com.marneux.marneweather.presentation.views.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marneux.marneweather.R
import com.marneux.marneweather.model.location.LocationAutofillSuggestion
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import com.marneux.marneweather.model.weather.HourlyForecast
import com.marneux.marneweather.presentation.views.home.composables.ShowPermissionWarningDialog
import com.marneux.marneweather.presentation.views.home.composables.currentWeatherCardItem
import com.marneux.marneweather.presentation.views.home.composables.errorCardItem
import com.marneux.marneweather.presentation.views.home.composables.prepareLocationPermissionRequest
import com.marneux.marneweather.presentation.views.home.composables.savedLocationItems
import com.marneux.marneweather.presentation.views.home.composables.searchBarItem
import com.marneux.marneweather.presentation.views.home.composables.subHeaderItem

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
    onRetryFetchWeatherCurrentLocation: () -> Unit = onLocationPermissionGranted
) {
    HomeScreen(
        modifier = modifier,
        weatherSavedLocations = homeScreenUiState.weatherSavedLocations,
        suggestionSearchQuery = homeScreenUiState.autofillSuggestions,
        isSuggestionLoading = homeScreenUiState.isLoadingAutofillSuggestions,
        isWeatherForecastLoading = homeScreenUiState.isLoadingWeatherCurrentLocation,
        isSavedLocationsLoading = homeScreenUiState.isLoadingSavedLocations,
        weatherCurrentLocation = homeScreenUiState.weatherDetailsCurrentLocation,
        hourlyForecastCurrentLocation = homeScreenUiState.hourlyForecastsCurrentLocation,
        errorFetchWeatherCurrentLocation = homeScreenUiState.errorFetchWeatherCurrentLocation,
        errorFetchWeatherSavedLocations = homeScreenUiState.errorFetchWeatherSavedLocations,
        errorLoadingAutofillSuggestions = homeScreenUiState.errorFetchAutofillSuggestions,
        onRetryFetchWeatherCurrentLocation = onRetryFetchWeatherCurrentLocation,
        onRetryFetchWeatherSavedLocations = onRetryFetchingWeatherForSavedLocations,
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
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class,

    )
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    weatherSavedLocations: List<BriefWeatherDetails>,
    suggestionSearchQuery: List<LocationAutofillSuggestion>,
    weatherCurrentLocation: BriefWeatherDetails?,
    hourlyForecastCurrentLocation: List<HourlyForecast>?,
    isSuggestionLoading: Boolean = false,
    isSavedLocationsLoading: Boolean = false,
    isWeatherForecastLoading: Boolean,
    errorFetchWeatherCurrentLocation: Boolean,
    errorFetchWeatherSavedLocations: Boolean,
    errorLoadingAutofillSuggestions: Boolean,
    onRetryFetchWeatherSavedLocations: () -> Unit,
    onRetryFetchWeatherCurrentLocation: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSuggestionClick: (LocationAutofillSuggestion) -> Unit,
    onSavedLocationItemClick: (BriefWeatherDetails) -> Unit,
    onSavedLocationDismissed: (BriefWeatherDetails) -> Unit,
    onLocationPermissionGranted: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: HomeViewModel = viewModel(),
) {


    val currentLocation = stringResource(id = R.string.home_current_location)
    val savedLocation = stringResource(id = R.string.home_saved_locations)
    val errorCurrentLoc = stringResource(id = R.string.error_current_loc)
    val errorSavedLoc = stringResource(id = R.string.error_saved_loc)

    val context = LocalContext.current

    var displayLocationWeatherSubHeader by remember { mutableStateOf(false) }
    var hasRequestedPermissions by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isSearchBarActive by remember { mutableStateOf(false) }
    var currentQueryText by remember { mutableStateOf("") }
    val clearQueryText = {
        currentQueryText = ""
        onSearchQueryChange("")
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing,
        onRefresh = { viewModel.refreshScreen() }
    )


    val locationPermissionLauncher = prepareLocationPermissionRequest(
        onPermissionGranted = {
            displayLocationWeatherSubHeader = true
            onLocationPermissionGranted()
        },
        onPermissionDenied = {
            displayLocationWeatherSubHeader = false
            showDialog = true
        }
    )

    ShowPermissionWarningDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onOpenSettings = {
            showDialog = false
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
    )



    LaunchedEffect(key1 = hasRequestedPermissions) {
        if (!hasRequestedPermissions) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
            hasRequestedPermissions = true
        }
    }

    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
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
                suggestionsForSearchQuery = suggestionSearchQuery,
                isSuggestionsListLoading = isSuggestionLoading,
                onSuggestionClick = onSuggestionClick
            )

            if (displayLocationWeatherSubHeader) {
                subHeaderItem(
                    title = currentLocation,
                    isLoadingAnimationVisible = isWeatherForecastLoading
                )
            }

            if (weatherCurrentLocation != null && hourlyForecastCurrentLocation != null) {
                currentWeatherCardItem(
                    weatherCurrentLocation = weatherCurrentLocation,
                    hourlyForecastsCurrentLocation = hourlyForecastCurrentLocation,
                    onClick = { onSavedLocationItemClick(weatherCurrentLocation) }
                )
            }

            if (errorFetchWeatherCurrentLocation) {
                errorCardItem(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    errorMessage = errorCurrentLoc,
                    onRetryButtonClick = onRetryFetchWeatherCurrentLocation
                )
            }

            subHeaderItem(
                title = savedLocation,
                isLoadingAnimationVisible = isSavedLocationsLoading
            )

            if (errorFetchWeatherSavedLocations) {
                errorCardItem(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    errorMessage = errorSavedLoc,
                    onRetryButtonClick = onRetryFetchWeatherSavedLocations
                )
            }

            savedLocationItems(
                savedLocationItemsList = weatherSavedLocations,
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
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = viewModel.isRefreshing,
            state = pullRefreshState
        )
    }
}







