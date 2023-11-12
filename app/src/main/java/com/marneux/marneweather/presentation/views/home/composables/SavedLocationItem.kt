package com.marneux.marneweather.presentation.views.home.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.model.weather.BriefWeatherDetails


@ExperimentalFoundationApi
@ExperimentalMaterial3Api
fun LazyListScope.savedLocationItems(
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
@Composable
private fun CompactWeatherCard(
    nameOfLocation: String,
    shortDescription: String,
    @DrawableRes shortDescriptionIcon: Int,
    weatherInDegrees: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val weatherWithDegreesSuperscript = remember(weatherInDegrees) {

        "$weatherInDegrees°"
    }
    OutlinedCard(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nameOfLocation,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                ShortWeatherDescriptionWithIconRow(
                    shortDescription = shortDescription,
                    iconRes = shortDescriptionIcon
                )
            }
            Text(
                text = weatherWithDegreesSuperscript,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

@Composable
private fun ShortWeatherDescriptionWithIconRow(
    shortDescription: String,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // fix de color usando "Color.Unspecified" para asegurarse de que ningun color es
        // aplicado al vector
        // Mirar documentacion Icon.
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            text = shortDescription,
            maxLines = 1,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal
        )
    }
}


@ExperimentalMaterial3Api
@Composable
private fun SwipeToDismissCompactWeatherCard(
    nameOfLocation: String,
    shortDescription: String,
    @DrawableRes shortDescriptionIcon: Int,
    weatherInDegrees: String,
    onClick: () -> Unit,
    dismissState: DismissState,
    modifier: Modifier = Modifier,
) {
    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        background = {},
        dismissContent = {
            CompactWeatherCard(
                nameOfLocation = nameOfLocation,
                shortDescription = shortDescription,
                shortDescriptionIcon = shortDescriptionIcon,
                weatherInDegrees = weatherInDegrees,
                onClick = onClick
            )
        },
        directions = setOf(DismissDirection.EndToStart)
    )
}
