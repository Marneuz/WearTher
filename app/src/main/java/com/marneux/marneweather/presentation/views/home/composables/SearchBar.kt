package com.marneux.marneweather.presentation.views.home.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer
import com.marneux.marneweather.R
import com.marneux.marneweather.model.location.AutoSuggestLocation

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
fun LazyListScope.searchBarItem(
    currentSearchQuery: String,
    isSearchBarActive: Boolean,
    isSuggestionsListLoading: Boolean,
    errorLoadingSuggestions: Boolean,
    suggestionsForSearchQuery: List<AutoSuggestLocation>,
    onClearSearchQueryIconClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchBarActiveChange: (Boolean) -> Unit,
    onSuggestionClick: (AutoSuggestLocation) -> Unit
) {
    item {
        val searchBarSuggestionsContent = @Composable {
            AutoFillSuggestionsList(
                suggestions = suggestionsForSearchQuery,
                onSuggestionClick = onSuggestionClick,
                isSuggestionsListLoading = isSuggestionsListLoading
            )
        }
        val errorSearchBarSuggestionsContent = @Composable {
            OutlinedCard(modifier = Modifier.padding(16.dp)) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.error_fetching_suggestions)
                )
            }
        }

        Header(
            modifier = Modifier.fillMaxWidth(),
            currentSearchQuery = currentSearchQuery,
            onClearSearchQueryIconClick = onClearSearchQueryIconClick,
            isSearchBarActive = isSearchBarActive,
            onSearchQueryChange = onSearchQueryChange,
            onSearchBarActiveChange = onSearchBarActiveChange,
            searchBarSuggestionsContent = {
                if (errorLoadingSuggestions) errorSearchBarSuggestionsContent()
                else searchBarSuggestionsContent()
            }
        )
    }
}

@Composable
private fun AutoFillSuggestionsList(
    suggestions: List<AutoSuggestLocation>,
    isSuggestionsListLoading: Boolean,
    onSuggestionClick: (AutoSuggestLocation) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (isSuggestionsListLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn {
                autofillSuggestionItems(
                    suggestions = suggestions,
                    onSuggestionClick = onSuggestionClick
                )
                item {
                    Spacer(modifier = Modifier.imePadding())
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
private fun Header(
    modifier: Modifier = Modifier,
    currentSearchQuery: String,
    onClearSearchQueryIconClick: () -> Unit,
    isSearchBarActive: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSearchBarActiveChange: (Boolean) -> Unit,
    searchBarSuggestionsContent: @Composable (ColumnScope.() -> Unit)
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Column(modifier = modifier) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .sizeIn(
                    maxWidth = screenWidth,
                    maxHeight = screenHeight
                ),
            query = currentSearchQuery,
            onQueryChange = onSearchQueryChange,
            onSearch = {

            },
            active = isSearchBarActive,
            onActiveChange = onSearchBarActiveChange,
            leadingIcon = {
                AnimatedSearchBarLeadingIcon(
                    isSearchBarActive = isSearchBarActive,
                    onSearchIconClick = { onSearchBarActiveChange(true) },
                    onBackIconClick = {

                        onClearSearchQueryIconClick()
                        onSearchBarActiveChange(false)
                    }
                )
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = isSearchBarActive,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it })
                ) {
                    val iconImageVector = Icons.Filled.Close
                    IconButton(
                        onClick = onClearSearchQueryIconClick,
                        content = { Icon(imageVector = iconImageVector, contentDescription = null) }
                    )
                }
            },
            placeholder = { Text(text = stringResource(R.string.home_search_location)) },
            content = searchBarSuggestionsContent
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun AnimatedSearchBarLeadingIcon(
    isSearchBarActive: Boolean,
    onSearchIconClick: () -> Unit,
    onBackIconClick: () -> Unit
) {
    AnimatedContent(
        targetState = isSearchBarActive,
        transitionSpec = {
            val isActive = this.targetState
            val slideIn = slideIntoContainer(
                if (isActive) AnimatedContentTransitionScope.SlideDirection.Start
                else AnimatedContentTransitionScope.SlideDirection.End
            )
            val slideOut = slideOutOfContainer(
                if (isActive) AnimatedContentTransitionScope.SlideDirection.Start
                else AnimatedContentTransitionScope.SlideDirection.End
            )
            slideIn togetherWith slideOut
        }, label = ""
    ) { isActive ->
        if (isActive) {
            IconButton(
                onClick = onBackIconClick,
                content = { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null) }
            )
        } else {
            IconButton(
                onClick = onSearchIconClick,
                content = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) }
            )
        }
    }
}


@Composable
private fun AutofillSuggestionLeadingIcon(countryFlagUrl: String) {
    val context = LocalContext.current
    val imageRequest = remember(countryFlagUrl) {
        ImageRequest.Builder(context)
            .data(countryFlagUrl)
            .decoderFactory(SvgDecoder.Factory())
            .build()
    }
    var isLoadingAnimationVisible by remember { mutableStateOf(false) }
    AsyncImage(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .placeholder(
                visible = isLoadingAnimationVisible,
                highlight = PlaceholderHighlight.shimmer()
            ),
        model = imageRequest,
        contentDescription = null,
        onState = { asyncPainterState ->
            isLoadingAnimationVisible = asyncPainterState is AsyncImagePainter.State.Loading
        }
    )
}

private fun LazyListScope.autofillSuggestionItems(
    suggestions: List<AutoSuggestLocation>,
    onSuggestionClick: (AutoSuggestLocation) -> Unit,
) {
    items(items = suggestions, key = { it.idLocation }) {
        AutofillSuggestion(
            title = it.nameLocation,
            subText = it.addressLocation,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = { onSuggestionClick(it) },
            leadingIcon = { AutofillSuggestionLeadingIcon(countryFlagUrl = it.countryFlag) }
        )
    }
}

@Composable
private fun AutofillSuggestion(
    title: String,
    subText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit = { DefaultLeadingIcon() }
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon()
        Spacer(modifier = Modifier.size(16.dp))
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = subText,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun DefaultLeadingIcon() {
    Icon(
        modifier = Modifier
            .size(40.dp)
            .drawBehind {
                drawCircle(
                    color = Color.White.copy(alpha = 0.3f),
                    center = center,
                    radius = size.minDimension / 1.7f
                )
            },
        imageVector = Icons.Filled.LocationOn,
        tint = Color.White,
        contentDescription = null
    )
}
