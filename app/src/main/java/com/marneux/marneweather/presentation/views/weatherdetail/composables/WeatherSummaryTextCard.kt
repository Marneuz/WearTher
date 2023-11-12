package com.marneux.marneweather.presentation.views.weatherdetail.composables

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.marneux.marneweather.R
import com.marneux.marneweather.presentation.theme.GreenGPT

@Composable
fun WeatherSummaryTextCard(
    modifier: Modifier = Modifier,
    isWeatherSummaryLoading: Boolean,
    summaryText: String,
) {
    Card(modifier = modifier) {
        val context = LocalContext.current
        val imageLoader = remember {
            ImageLoader.Builder(context = context)
                .components {
                    if (Build.VERSION.SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()
        }
        val imageRequest = remember {
            ImageRequest.Builder(context = context)
                .data(R.drawable.loading_world)
                .size(Size.ORIGINAL)
                .build()
        }
        val asyncImagePainter = rememberAsyncImagePainter(
            model = imageRequest,
            imageLoader = imageLoader
        )

        Row(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isWeatherSummaryLoading) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = asyncImagePainter,
                    contentDescription = null
                )
            } else {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_gpt_icon),
                    tint = GreenGPT,
                    contentDescription = null
                )
            }
            Text(
                text = stringResource(R.string.detail_assistant),
                style = MaterialTheme.typography.titleMedium
            )
        }
        TypingAnimatedText(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = summaryText
        )
    }
}