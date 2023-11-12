package com.marneux.marneweather.presentation.views.weatherdetail.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.R
import com.marneux.marneweather.model.weather.PrecipitationProbability
import com.marneux.marneweather.presentation.common.model.hourStringInTwelveHourFormat

@Composable
fun PrecipitationProbabilitiesCard(
    precipitationProbabilities: List<PrecipitationProbability>,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_chance_of_rain),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.detail_chance_of_rain),
                style = MaterialTheme.typography.titleMedium
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(precipitationProbabilities) {
                ProbabilityProgressColumn(
                    modifier = Modifier.padding(bottom = 16.dp),
                    precipitationProbability = it
                )
            }
        }
    }
}

@Composable
private fun ProbabilityProgressColumn(
    precipitationProbability: PrecipitationProbability,
    modifier: Modifier = Modifier
) {
    var progressValue by remember { mutableStateOf(0f) }
    val animatedProgressValue by animateFloatAsState(targetValue = progressValue, label = "")
    LaunchedEffect(precipitationProbability) {
        progressValue = precipitationProbability.probabilityPercentage / 100f
    }
    val (heightOfProgressBarWhenVertical, widthOfProgressBarWhenVertical) = remember {
        Pair(120.dp, 16.dp)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = precipitationProbability.dateTime.hourStringInTwelveHourFormat.padStart(length = 5),
            style = MaterialTheme.typography.labelLarge
        )
        Box(
            modifier = Modifier.size(
                height = heightOfProgressBarWhenVertical,
                width = widthOfProgressBarWhenVertical
            )
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .requiredSize(
                        height = widthOfProgressBarWhenVertical,
                        width = heightOfProgressBarWhenVertical
                    )
                    .rotate(-90f),
                progress = animatedProgressValue,
                strokeCap = StrokeCap.Round,
                trackColor = ProgressIndicatorDefaults.linearColor.copy(alpha = 0.5f)
            )
        }
        Text(
            text = "${precipitationProbability.probabilityPercentage}%".padStart(length = 4),
            style = MaterialTheme.typography.labelLarge
        )
    }
}