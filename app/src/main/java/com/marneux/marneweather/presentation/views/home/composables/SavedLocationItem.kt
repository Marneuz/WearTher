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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.R
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import com.marneux.marneweather.presentation.theme.MarneTheme


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
            nameLocation = it.nameLocation,
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
    nameLocation: String,
    shortDescription: String,
    @DrawableRes shortDescriptionIcon: Int,
    weatherInDegrees: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val weatherDegress = remember(weatherInDegrees) {

        "$weatherInDegrees°"
    }
    OutlinedCard(
        modifier = modifier.shadow(
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp),
            ambientColor = Color.Blue,
            spotColor = Color.Cyan
        ),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        elevation = CardDefaults.outlinedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nameLocation,
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
                text = weatherDegress,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

@Composable
fun ShortWeatherDescriptionWithIconRow(
    shortDescription: String,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
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
    nameLocation: String,
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
                nameLocation = nameLocation,
                shortDescription = shortDescription,
                shortDescriptionIcon = shortDescriptionIcon,
                weatherInDegrees = weatherInDegrees,
                onClick = onClick
            )
        },
        directions = setOf(DismissDirection.EndToStart)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun SavedLocationPreview() {
    MarneTheme {
        Surface {
            CompactWeatherCard(
                nameLocation = "Zaragoza",
                shortDescription = "Clean",
                shortDescriptionIcon = R.drawable.ic_day_clear,
                weatherInDegrees = "19",
                onClick = {}
            )
        }
    }
}