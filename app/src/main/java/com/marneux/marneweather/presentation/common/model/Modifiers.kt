package com.marneux.marneweather.presentation.common.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

fun Modifier.leftRightShading() = composed {
    this.fadingEdge(
        brush = Brush.horizontalGradient(
            0f to Color.Transparent,
            0.125f to MaterialTheme.colorScheme.surface,
            0.875f to MaterialTheme.colorScheme.surface,
            1f to Color.Transparent
        )
    )
}

fun Modifier.rightShading() = composed {
    this.fadingEdge(
        Brush.horizontalGradient(
            0f to MaterialTheme.colorScheme.surface,
            0.875f to MaterialTheme.colorScheme.surface,
            1f to Color.Transparent,
        )
    )
}