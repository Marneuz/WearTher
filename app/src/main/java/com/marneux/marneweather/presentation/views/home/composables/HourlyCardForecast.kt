package com.marneux.marneweather.presentation.views.home.composables

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.R
import com.marneux.marneweather.model.weather.HourlyForecast
import com.marneux.marneweather.presentation.common.model.hourTwelveHourFormat
import com.marneux.marneweather.presentation.theme.MarneTheme
import java.time.LocalDateTime


@Composable
fun HourlyForecastCard(
    nameLocation: String,
    shortDescription: String,
    @DrawableRes shortDescriptionIcon: Int,
    weatherInDegrees: String,
    onClick: () -> Unit,
    hourlyForecasts: List<HourlyForecast>,
    modifier: Modifier = Modifier
) {
    val weatherWithDegreesSuperscript = remember(weatherInDegrees) {
        "$weatherInDegrees°"
    }

    OutlinedCard(
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Blue,
                spotColor = Color.Cyan
            )
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            HeaderSection(nameLocation, shortDescription, shortDescriptionIcon)
            WeatherDegreeSection(weatherWithDegreesSuperscript)
            HourlyForecastSection(hourlyForecasts)
        }
    }
}

@Composable
private fun HeaderSection(
    nameLocation: String,
    shortDescription: String,
    iconRes: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = nameLocation,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
            ShortWeatherDescriptionWithIconRow(
                shortDescription = shortDescription,
                iconRes = iconRes
            )
        }
    }
}

@Composable
private fun WeatherDegreeSection(weatherWithDegreesSuperscript: String) {
    Text(
        text = weatherWithDegreesSuperscript,
        style = MaterialTheme.typography.displayMedium,
        textAlign = TextAlign.End
    )
}

@Composable
private fun HourlyForecastSection(hourlyForecasts: List<HourlyForecast>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(hourlyForecasts) { forecast ->
            HourlyForecastItem(
                dateTime = forecast.dateTime,
                iconResId = forecast.weatherIconResId,
                temperature = forecast.temperature
            )
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
            style = MaterialTheme.typography.labelLarge
        )
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = ImageVector.vectorResource(id = iconResId),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            text = "${temperature}°",
            style = MaterialTheme.typography.labelLarge
        )
    }
}


@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES) // para ponerlo modo noche en caso de tener
// dia y noche
@Composable
private fun HourlyForecastCardPreview() {
    MarneTheme {
        Surface {
            val mockHourlyForecasts = listOf(
                HourlyForecast(LocalDateTime.now(), R.drawable.ic_day_clear, 18),
                HourlyForecast(LocalDateTime.now().plusHours(1), R.drawable.ic_day_rain, 20),
                HourlyForecast(LocalDateTime.now().plusHours(2), R.drawable.ic_day_few_clouds, 17)
            )

            HourlyForecastCard(
                nameLocation = "Zaragoza",
                shortDescription = "Fog",
                shortDescriptionIcon = R.drawable.ic_day_clear,
                weatherInDegrees = "17",
                onClick = { },
                hourlyForecasts = mockHourlyForecasts
            )
        }
    }
}
