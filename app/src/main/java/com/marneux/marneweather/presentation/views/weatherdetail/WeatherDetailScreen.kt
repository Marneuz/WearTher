package com.marneux.marneweather.presentation.views.weatherdetail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.R
import com.marneux.marneweather.model.weather.HourlyForecast
import com.marneux.marneweather.model.weather.PrecipitationProbability
import com.marneux.marneweather.model.weather.SingleWeatherDetail
import com.marneux.marneweather.presentation.views.weatherdetail.composables.Header
import com.marneux.marneweather.presentation.views.weatherdetail.composables.HourlyForecastCard
import com.marneux.marneweather.presentation.views.weatherdetail.composables.PrecipitationProbabilitiesCard
import com.marneux.marneweather.presentation.views.weatherdetail.composables.SingleWeatherDetailCard
import com.marneux.marneweather.presentation.views.weatherdetail.composables.WeatherSummaryTextCard

@Composable
fun WeatherDetailScreen(
    uiState: WeatherDetailScreenUiState,
    snackbarHostState: SnackbarHostState,
    onSaveButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
) {
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            content = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) }
        )
    } else if (uiState.errorMessage != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                text = uiState.errorMessage
            )
            Button(onClick = onBackButtonClick, content = { Text(stringResource(R.string.go_back)) })
        }
    } else {
        WeatherDetailScreen(
            snackbarHostState = snackbarHostState,
            nameLocation = uiState.weatherDetailsOfChosenLocation!!.nameLocation,
            weatherConditionImage = uiState.weatherDetailsOfChosenLocation.imageResId,
            weatherConditionIconId = uiState.weatherDetailsOfChosenLocation.iconResId,
            weatherInDegrees = uiState.weatherDetailsOfChosenLocation.temperatureRoundedToInt,
            weatherCondition = uiState.weatherDetailsOfChosenLocation.weatherCondition,
            aiGeneratedWeatherSummaryText = uiState.weatherSummaryText,
            isPreviouslySavedLocation = uiState.isPreviouslySavedLocation,
            isWeatherSummaryLoading = uiState.isWeatherSummaryTextLoading,
            singleWeatherDetails = uiState.additionalWeatherInfoItems,
            hourlyForecasts = uiState.hourlyForecasts,
            precipitationProbabilities = uiState.precipitationProbabilities,
            onBackButtonClick = onBackButtonClick,
            onSaveButtonClick = onSaveButtonClick,
        )
    }
}

@Composable
fun WeatherDetailScreen(
    nameLocation: String,
    weatherCondition: String,
    aiGeneratedWeatherSummaryText: String?,
    @DrawableRes weatherConditionImage: Int,
    @DrawableRes weatherConditionIconId: Int,
    weatherInDegrees: Int,
    isWeatherSummaryLoading: Boolean,
    isPreviouslySavedLocation: Boolean,
    onBackButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    singleWeatherDetails: List<SingleWeatherDetail>,
    hourlyForecasts: List<HourlyForecast>,
    precipitationProbabilities: List<PrecipitationProbability>,
    snackbarHostState: SnackbarHostState,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Box {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Header(
                    modifier = Modifier
                        .requiredWidth(screenWidth)
                        .height(350.dp),
                    headerImageResId = weatherConditionImage,
                    weatherConditionIconId = weatherConditionIconId,
                    onBackButtonClick = onBackButtonClick,
                    shouldDisplaySaveButton = !isPreviouslySavedLocation,
                    onSaveButtonClick = onSaveButtonClick,
                    nameLocation = nameLocation,
                    currentWeatherInDegrees = weatherInDegrees,
                    weatherCondition = weatherCondition
                )
            }

            if (aiGeneratedWeatherSummaryText != null || isWeatherSummaryLoading) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    WeatherSummaryTextCard(
                        summaryText = aiGeneratedWeatherSummaryText ?: stringResource(
                            id = R
                                .string.detail_loading
                        ),
                        isWeatherSummaryLoading = isWeatherSummaryLoading
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                HourlyForecastCard(hourlyForecasts = hourlyForecasts)
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                PrecipitationProbabilitiesCard(precipitationProbabilities = precipitationProbabilities)
            }
            items(singleWeatherDetails) {
                SingleWeatherDetailCard(
                    name = it.name,
                    value = it.value,
                    iconResId = it.iconResId
                )
            }
            item {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }

        SnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            hostState = snackbarHostState
        )
    }
}



