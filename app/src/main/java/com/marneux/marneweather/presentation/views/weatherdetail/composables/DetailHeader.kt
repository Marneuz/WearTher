package com.marneux.marneweather.presentation.views.weatherdetail.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    modifier: Modifier = Modifier,
    @DrawableRes headerImageResId: Int,
    @DrawableRes weatherConditionIconId: Int,
    onBackButtonClick: () -> Unit,
    shouldDisplaySaveButton: Boolean,
    onSaveButtonClick: () -> Unit,
    nameLocation: String,
    currentWeatherInDegrees: Int,
    weatherCondition: String,
) {
    Box(modifier = modifier) {
        val iconButtonContainerColor = remember {
            Color.Black.copy(0.4f)
        }
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = headerImageResId),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
        )

        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding(),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = iconButtonContainerColor
            ),
            onClick = onBackButtonClick
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = null
            )
        }
        if (shouldDisplaySaveButton) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding(),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = iconButtonContainerColor
                ),
                onClick = onSaveButtonClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                text = nameLocation,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$currentWeatherInDegrees°",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 80.sp)
            )
            Box(
                modifier = Modifier
                    .background(Color.Black, shape = RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .offset(x = (-8).dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = ImageVector.vectorResource(id = weatherConditionIconId),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Text(
                        text = weatherCondition,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}