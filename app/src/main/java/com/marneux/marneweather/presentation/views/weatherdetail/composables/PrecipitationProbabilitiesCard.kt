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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.R
import com.marneux.marneweather.model.weather.RainChances
import com.marneux.marneweather.presentation.common.model.hourTwelveHourFormat
import com.marneux.marneweather.presentation.common.model.leftRightShading
import com.marneux.marneweather.presentation.common.model.rememberIsFadeBothEdges
import com.marneux.marneweather.presentation.common.model.rightShading
import com.marneux.marneweather.presentation.theme.MarneTheme
import java.time.LocalDateTime

@Composable
fun PrecipitationProbabilitiesCard(
    precipitationProbabilities: List<RainChances>,
    modifier: Modifier = Modifier,
) {

    val listState = rememberLazyListState()
    val fadeBothEdges =
        rememberIsFadeBothEdges(listState = listState, dataList = precipitationProbabilities)

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
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .then(
                    if (fadeBothEdges) Modifier.leftRightShading()
                    else Modifier.rightShading()
                ),
            state = listState
        ) {
            items(precipitationProbabilities) {
                ProbabilityProgressColumn(
                    modifier = Modifier.padding(bottom = 16.dp),
                    rainChances = it
                )
            }
        }
    }
}

@Composable
private fun ProbabilityProgressColumn(
    rainChances: RainChances,
    modifier: Modifier
) {
    var progressValue by remember { mutableFloatStateOf(0f) }
    val animatedProgressValue by animateFloatAsState(targetValue = progressValue, label = "")
    LaunchedEffect(rainChances) {
        progressValue = rainChances.probabilityPercentage / 100f
    }
    val (heightProgressBar, widthProgressBar) = remember {
        Pair(120.dp, 16.dp)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = rainChances.dateTime.hourTwelveHourFormat.padStart(length = 3),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.alpha(0.75f)
        )
        Box(
            modifier = Modifier.size(
                height = heightProgressBar,
                width = widthProgressBar
            )
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .requiredSize(
                        height = widthProgressBar,
                        width = heightProgressBar
                    )
                    .rotate(-90f),
                progress = animatedProgressValue,
                strokeCap = StrokeCap.Round,
                trackColor = ProgressIndicatorDefaults.linearColor.copy(alpha = 0.35f)
            )
        }
        Text(
            text = "${rainChances.probabilityPercentage}%".padStart(length = 4),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(wallpaper = Wallpapers.NONE)
@Composable
private fun PrecipitationPreview() {
    MarneTheme {
        Surface {
            val mockRainProbability = listOf(
                RainChances("1", "100", LocalDateTime.now(), 25),
                RainChances("16", "100", LocalDateTime.now().plusHours(1), 80),
                RainChances("16", "16", LocalDateTime.now().plusHours(2), 50),
                RainChances("16", "100", LocalDateTime.now().plusHours(3), 80),
                RainChances("16", "16", LocalDateTime.now().plusHours(4), 50),
                RainChances("16", "100", LocalDateTime.now().plusHours(5), 80),
            )
            PrecipitationProbabilitiesCard(mockRainProbability)
        }
    }
}
