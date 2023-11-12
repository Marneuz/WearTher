package com.marneux.marneweather.presentation.views.home.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import com.marneux.marneweather.model.weather.HourlyForecast

@ExperimentalFoundationApi
fun LazyListScope.currentWeatherCardItem(
    weatherCurrentLocation: BriefWeatherDetails,
    hourlyForecastsCurrentLocation: List<HourlyForecast>,
    onClick: () -> Unit,
) {
    item {
        HourlyForecastCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .animateItemPlacement(),
            nameLocation = weatherCurrentLocation.nameLocation,
            shortDescription = weatherCurrentLocation.shortDescription,
            shortDescriptionIcon = weatherCurrentLocation.shortDescriptionIcon,
            weatherInDegrees = weatherCurrentLocation.currentTemperatureRoundedToInt.toString(),
            onClick = onClick,
            hourlyForecasts = hourlyForecastsCurrentLocation,
        )
    }
}
