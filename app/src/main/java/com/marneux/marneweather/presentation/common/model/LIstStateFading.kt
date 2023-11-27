package com.marneux.marneweather.presentation.common.model

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun <T> rememberIsFadeBothEdges(
    listState: LazyListState,
    dataList: List<T>
): Boolean {
    val fadeBothEdges by remember(dataList.size) {
        derivedStateOf {
            val firstVisibleIndex = listState.firstVisibleItemIndex
            if (dataList.isNotEmpty()) {
                val firstVisibleItem = dataList[firstVisibleIndex]
                firstVisibleItem != dataList[0] || listState.isScrollInProgress
            } else {
                true
            }
        }
    }
    return fadeBothEdges
}