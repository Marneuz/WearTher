package com.marneux.marneweather.ui.views.home.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import com.marneux.marneweather.model.weather.HourlyForecast

@ExperimentalFoundationApi
fun LazyListScope.currentWeatherDetailCardItem(
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
