package com.marneux.marneweather.ui.views.home.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.R

@ExperimentalFoundationApi
fun LazyListScope.errorCardItem(
    errorMessage: String,
    modifier: Modifier = Modifier,
    onRetryButtonClick: () -> Unit
) {

    item {
        OutlinedCard(modifier = modifier.animateItemPlacement()) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = errorMessage,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedButton(
                    onClick = onRetryButtonClick,
                    content = { Text(text = stringResource(id = R.string.retry)) })
            }
        }
    }
}
