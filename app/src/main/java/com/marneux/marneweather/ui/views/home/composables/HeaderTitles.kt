package com.marneux.marneweather.ui.views.home.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

fun LazyListScope.subHeaderItem(title: String, isLoadingAnimationVisible: Boolean) {
    item {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(end = 8.dp),
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Normal
            )
            if (isLoadingAnimationVisible) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}