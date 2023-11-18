package com.marneux.marneweather.presentation.views.home.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import com.marneux.marneweather.model.weather.HourlyForecast
import com.marneux.marneweather.presentation.common.model.getWeatherIconResForCode
import com.marneux.marneweather.presentation.common.model.weatherCodeToDescriptionMap


@ExperimentalFoundationApi
fun LazyListScope.currentWeatherCardItem(
    weatherCurrentLocation: BriefWeatherDetails,
    hourlyForecast: List<HourlyForecast>,
    onClick: () -> Unit,
) {
    item {
        HourlyForecastCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .animateItemPlacement(),
            nameLocation = weatherCurrentLocation.nameLocation,
            shortDescription = weatherCodeToDescriptionMap.getValue(
                weatherCurrentLocation
                    .shortDescriptionCode
            ),
            shortDescriptionIcon = getWeatherIconResForCode(
                weatherCurrentLocation
                    .shortDescriptionCode
            ),
            weatherInDegrees = weatherCurrentLocation.temperatureRoundedToInt.toString(),
            onClick = onClick,
            hourlyForecasts = hourlyForecast,
        )
    }
}
