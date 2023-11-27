package com.marneux.marneweather.presentation.views.weatherdetail.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.R
import com.marneux.marneweather.model.weather.WeatherItem
import com.marneux.marneweather.presentation.common.model.weatherItemToIconResMap
import com.marneux.marneweather.presentation.common.model.weatherItemToStringResMap
import com.marneux.marneweather.presentation.theme.MarneTheme

@Composable
fun SingleWeatherDetailCard(
    itemType: WeatherItem,
    value: String,
    modifier: Modifier = Modifier
) {
    val notFound = stringResource(R.string.icon_not_found_for)
    val singleIconDetail = remember(itemType) {
        weatherItemToIconResMap[itemType] ?: error("$notFound $itemType")
    }
    val singleNameDetail = remember(itemType) {
        weatherItemToStringResMap[itemType] ?: error("$notFound $itemType")
    }


    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(id = singleIconDetail),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(
                    text = stringResource(id = singleNameDetail),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    minLines = 1,
                    modifier = Modifier.alpha(alpha = 0.75f)
                )
            }
        }
    }
}

@Preview
@Composable
fun SingleDetailPreview() {
    MarneTheme {
        Surface {
            SingleWeatherDetailCard(itemType = WeatherItem.MIN_TEMP, value = "21")
        }
    }
}