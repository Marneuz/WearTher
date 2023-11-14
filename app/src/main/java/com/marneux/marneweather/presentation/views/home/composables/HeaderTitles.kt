package com.marneux.marneweather.presentation.views.home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.presentation.theme.MarneTheme

fun LazyListScope.subHeaderItem(title: String, isLoadingAnimationVisible: Boolean) {
    item {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 2000)
                )
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
            if (isLoadingAnimationVisible) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 8.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

@Preview
@Composable
private fun HeaderPreview() {
    MarneTheme {
        Surface {
            LazyRow {
                subHeaderItem(
                    title = "Current Location",
                    isLoadingAnimationVisible = true,

                    )
            }
        }
    }
}