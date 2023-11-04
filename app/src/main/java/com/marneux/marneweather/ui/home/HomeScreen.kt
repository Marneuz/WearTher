package com.marneux.marneweather.ui.home


import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer
import com.marneux.marneweather.R
import com.marneux.marneweather.domain.models.location.LocationAutofillSuggestion
import com.marneux.marneweather.domain.models.weather.BriefWeatherDetails
import com.marneux.marneweather.domain.models.weather.HourlyForecast
import com.marneux.marneweather.ui.components.AutofillSuggestion
import com.marneux.marneweather.ui.components.CompactWeatherCardWithHourlyForecast
import com.marneux.marneweather.ui.components.SwipeToDismissCompactWeatherCard


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
        snackbarHostState = snackbarHostState,
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
        onLocationPermissionGranted = onLocationPermissionGranted
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
    snackbarHostState: SnackbarHostState
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


@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
private fun Header(
    modifier: Modifier = Modifier,
    currentSearchQuery: String,
    onClearSearchQueryIconClick: () -> Unit,
    isSearchBarActive: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSearchBarActiveChange: (Boolean) -> Unit,
    searchBarSuggestionsContent: @Composable (ColumnScope.() -> Unit)
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Column(modifier = modifier) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .sizeIn(
                    maxWidth = screenWidth,
                    maxHeight = screenHeight
                ),
            query = currentSearchQuery,
            onQueryChange = onSearchQueryChange,
            onSearch = {

            },
            active = isSearchBarActive,
            onActiveChange = onSearchBarActiveChange,
            leadingIcon = {
                AnimatedSearchBarLeadingIcon(
                    isSearchBarActive = isSearchBarActive,
                    onSearchIconClick = { onSearchBarActiveChange(true) },
                    onBackIconClick = {

                        onClearSearchQueryIconClick()
                        onSearchBarActiveChange(false)
                    }
                )
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = isSearchBarActive,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it })
                ) {
                    val iconImageVector = Icons.Filled.Close
                    IconButton(
                        onClick = onClearSearchQueryIconClick,
                        content = { Icon(imageVector = iconImageVector, contentDescription = null) }
                    )
                }
            },
            placeholder = { Text(text = stringResource(R.string.search_location)) },
            content = searchBarSuggestionsContent
        )
    }
}


@ExperimentalAnimationApi
@Composable
private fun AnimatedSearchBarLeadingIcon(
    isSearchBarActive: Boolean,
    onSearchIconClick: () -> Unit,
    onBackIconClick: () -> Unit
) {
    AnimatedContent(
        targetState = isSearchBarActive,
        transitionSpec = {
            val isActive = this.targetState
            val slideIn = slideIntoContainer(
                if (isActive) AnimatedContentTransitionScope.SlideDirection.Start
                else AnimatedContentTransitionScope.SlideDirection.End
            )
            val slideOut = slideOutOfContainer(
                if (isActive) AnimatedContentTransitionScope.SlideDirection.Start
                else AnimatedContentTransitionScope.SlideDirection.End
            )
            slideIn togetherWith slideOut
        }, label = ""
    ) { isActive ->
        if (isActive) {
            IconButton(
                onClick = onBackIconClick,
                content = { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null) }
            )
        } else {
            IconButton(
                onClick = onSearchIconClick,
                content = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) }
            )
        }
    }
}

@Composable
private fun AutoFillSuggestionsList(
    suggestions: List<LocationAutofillSuggestion>,
    isSuggestionsListLoading: Boolean,
    onSuggestionClick: (LocationAutofillSuggestion) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (isSuggestionsListLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn {
                autofillSuggestionItems(
                    suggestions = suggestions,
                    onSuggestionClick = onSuggestionClick
                )
                item {
                    Spacer(modifier = Modifier.imePadding())
                }
            }
        }
    }
}

private fun LazyListScope.autofillSuggestionItems(
    suggestions: List<LocationAutofillSuggestion>,
    onSuggestionClick: (LocationAutofillSuggestion) -> Unit,
) {
    items(items = suggestions, key = { it.idLocation }) {
        AutofillSuggestion(
            title = it.nameLocation,
            subText = it.addressLocation,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = { onSuggestionClick(it) },
            leadingIcon = { AutofillSuggestionLeadingIcon(countryFlagUrl = it.countryFlag) }
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
private fun LazyListScope.savedLocationItems(
    savedLocationItemsList: List<BriefWeatherDetails>,
    onSavedLocationItemClick: (BriefWeatherDetails) -> Unit,
    onSavedLocationDismissed: (BriefWeatherDetails) -> Unit
) {
    items(
        items = savedLocationItemsList,
        key = { it.nameLocation }
    ) {

        val dismissState = remember {
            DismissState(
                initialValue = DismissValue.Default,
                confirmValueChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToStart) {
                        onSavedLocationDismissed(it)
                        true
                    } else {
                        false
                    }
                }
            )
        }
        SwipeToDismissCompactWeatherCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .animateItemPlacement(),
            nameOfLocation = it.nameLocation,
            shortDescription = it.shortDescription,
            shortDescriptionIcon = it.shortDescriptionIcon,
            weatherInDegrees = it.currentTemperatureRoundedToInt.toString(),
            onClick = { onSavedLocationItemClick(it) },
            dismissState = dismissState
        )
    }
}

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
private fun LazyListScope.searchBarItem(
    currentSearchQuery: String,
    isSearchBarActive: Boolean,
    isSuggestionsListLoading: Boolean,
    errorLoadingSuggestions: Boolean,
    suggestionsForSearchQuery: List<LocationAutofillSuggestion>,
    onClearSearchQueryIconClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchBarActiveChange: (Boolean) -> Unit,
    onSuggestionClick: (LocationAutofillSuggestion) -> Unit
) {
    item {
        val searchBarSuggestionsContent = @Composable {
            AutoFillSuggestionsList(
                suggestions = suggestionsForSearchQuery,
                onSuggestionClick = onSuggestionClick,
                isSuggestionsListLoading = isSuggestionsListLoading
            )
        }
        val errorSearchBarSuggestionsContent = @Composable {
            OutlinedCard(modifier = Modifier.padding(16.dp)) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.error_fetching_suggestions)
                )
            }
        }

        Header(
            modifier = Modifier.fillMaxWidth(),
            currentSearchQuery = currentSearchQuery,
            onClearSearchQueryIconClick = onClearSearchQueryIconClick,
            isSearchBarActive = isSearchBarActive,
            onSearchQueryChange = onSearchQueryChange,
            onSearchBarActiveChange = onSearchBarActiveChange,
            searchBarSuggestionsContent = {
                if (errorLoadingSuggestions) errorSearchBarSuggestionsContent()
                else searchBarSuggestionsContent()
            }
        )
    }
}

private fun LazyListScope.subHeaderItem(title: String, isLoadingAnimationVisible: Boolean) {
    item {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(end = 8.dp),
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Normal
            )
            if (isLoadingAnimationVisible) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

@ExperimentalFoundationApi
private fun LazyListScope.currentWeatherDetailCardItem(
    weatherOfCurrentUserLocation: BriefWeatherDetails,
    hourlyForecastsOfCurrentUserLocation: List<HourlyForecast>,
    onClick: () -> Unit,
) {
    item {
        CompactWeatherCardWithHourlyForecast(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .animateItemPlacement(),
            nameOfLocation = weatherOfCurrentUserLocation.nameLocation,
            shortDescription = weatherOfCurrentUserLocation.shortDescription,
            shortDescriptionIcon = weatherOfCurrentUserLocation.shortDescriptionIcon,
            weatherInDegrees = weatherOfCurrentUserLocation.currentTemperatureRoundedToInt.toString(),
            onClick = onClick,
            hourlyForecasts = hourlyForecastsOfCurrentUserLocation
        )
    }
}

@Composable
private fun AutofillSuggestionLeadingIcon(countryFlagUrl: String) {
    val context = LocalContext.current
    val imageRequest = remember(countryFlagUrl) {
        ImageRequest.Builder(context)
            .data(countryFlagUrl)
            .decoderFactory(SvgDecoder.Factory())
            .build()
    }
    var isLoadingAnimationVisible by remember { mutableStateOf(false) }
    AsyncImage(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .placeholder(
                visible = isLoadingAnimationVisible,
                highlight = PlaceholderHighlight.shimmer()
            ),
        model = imageRequest,
        contentDescription = null,
        onState = { asyncPainterState ->
            isLoadingAnimationVisible = asyncPainterState is AsyncImagePainter.State.Loading
        }
    )
}

@ExperimentalFoundationApi
private fun LazyListScope.errorCardItem(
    errorMessage: String,
    modifier: Modifier = Modifier,
    onRetryButtonClick: () -> Unit
) {

    item {
        OutlinedCard(modifier = modifier.animateItemPlacement()) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = errorMessage,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedButton(
                    onClick = onRetryButtonClick,
                    content = { Text(text = stringResource(id = R.string.retry)) })
            }
        }
    }
}
