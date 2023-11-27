package com.marneux.marneweather.presentation.views.weatherdetail.composables


import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.R
import com.marneux.marneweather.model.weather.HourlyForecast
import com.marneux.marneweather.presentation.common.model.getWeatherIconResForCode
import com.marneux.marneweather.presentation.common.model.hourTwelveHourFormat
import com.marneux.marneweather.presentation.common.model.leftRightShading
import com.marneux.marneweather.presentation.common.model.rememberIsFadeBothEdges
import com.marneux.marneweather.presentation.common.model.rightShading
import com.marneux.marneweather.presentation.theme.MarneTheme
import java.time.LocalDateTime

@Composable
fun HourlyForecastCard(
    hourlyForecasts: List<HourlyForecast>,
    modifier: Modifier = Modifier
) {

    val listState = rememberLazyListState()
    val fadeBothEdges = rememberIsFadeBothEdges(listState = listState, dataList = hourlyForecasts)
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_schedule_24),
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = stringResource(R.string.detail_hourly_forecast),
                style = MaterialTheme.typography.titleMedium
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .then(
                    if (fadeBothEdges)
                        Modifier.leftRightShading() else Modifier.rightShading()
                ),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(hourlyForecasts) {
                HourlyForecastItem(
                    dateTime = it.dateTime,
                    iconResId = getWeatherIconResForCode(it.iconDescriptionCode),
                    temperature = it.temperature
                )
            }
        }
    }
}

@Composable
private fun HourlyForecastItem(
    modifier: Modifier = Modifier,
    dateTime: LocalDateTime,
    @DrawableRes iconResId: Int,
    temperature: Int
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dateTime.hourTwelveHourFormat,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.alpha(0.75f)
        )
        Icon(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            text = "${temperature}°",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Composable
private fun HourlyForecastPreview() {
    MarneTheme {
        Surface {
            val mockHourlyForecasts = listOf(
                HourlyForecast(
                    LocalDateTime.now(),
                    temperature = 19, iconDescriptionCode = 0
                ),
                HourlyForecast(
                    LocalDateTime.now().plusHours(1),
                    temperature = 28, iconDescriptionCode = 1
                ),
                HourlyForecast(
                    LocalDateTime.now().plusHours(2),
                    temperature = -5, iconDescriptionCode = 2
                )
            )
            HourlyForecastCard(
                hourlyForecasts = mockHourlyForecasts
            )
        }
    }
}