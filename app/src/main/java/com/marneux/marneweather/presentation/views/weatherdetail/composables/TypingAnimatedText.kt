package com.marneux.marneweather.presentation.views.weatherdetail.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay

@Composable
fun TypingAnimatedText(
    text: String,
    modifier: Modifier = Modifier,
    delayBetweenEachChar: Long = 50L,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme
            .onSurface, textMotion = TextMotion.Animated
    ),
    maxLines: Int = Int.MAX_VALUE
) {
    var currentText by rememberSaveable(text) { mutableStateOf("") }
    var currentIndex by rememberSaveable(text) { mutableIntStateOf(0) }
    LaunchedEffect(text) {
        while (currentIndex in text.indices) {
            currentText += text[currentIndex]
            currentIndex++
            delay(delayBetweenEachChar)
        }
    }
    Text(
        text = currentText,
        modifier = modifier,
        style = textStyle,
        maxLines = maxLines,
        overflow = TextOverflow.Clip
    )
}
