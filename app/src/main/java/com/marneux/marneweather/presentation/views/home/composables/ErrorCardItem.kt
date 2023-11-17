package com.marneux.marneweather.presentation.views.home.composables

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.marneux.marneweather.R
import com.marneux.marneweather.presentation.theme.MarneTheme


@ExperimentalFoundationApi
fun LazyListScope.errorCardItem(
    errorMessage: String,
    modifier: Modifier = Modifier,
    onRetryButtonClick: () -> Unit
) {

    item {
        OutlinedCard(
            modifier = modifier
                .animateItemPlacement()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color.Magenta,
                    spotColor = Color.Red
                ),
            shape = RoundedCornerShape(20.dp),


            ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = errorMessage,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.size(16.dp))
                ElevatedButton(
                    onClick = onRetryButtonClick,
                    content = {
                        Text(text = stringResource(id = R.string.retry))
                    },
                    elevation = ButtonDefaults.elevatedButtonElevation(10.dp)
                )
            }
        }
    }
}

@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES) // para ponerlo modo noche en caso de tener
// dia y noche
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ErrorCardItemPreview() {
    MarneTheme {
        Surface {
            LazyColumn {
                errorCardItem(
                    errorMessage = "An error occurred. Please try again.",
                    onRetryButtonClick = { /* Implement retry logic here */ }
                )
            }
        }
    }
}