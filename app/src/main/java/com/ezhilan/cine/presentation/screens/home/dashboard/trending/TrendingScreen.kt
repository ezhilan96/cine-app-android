@file:OptIn(ExperimentalMaterial3Api::class)

package com.ezhilan.cine.presentation.screens.home.dashboard.trending

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ezhilan.cine.R
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.presentation.config.CineTheme
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.config.textStyle
import com.ezhilan.cine.presentation.screens.core.component.HorizontalListContainer
import com.ezhilan.cine.presentation.screens.core.component.PullToRefreshContainer
import com.ezhilan.cine.presentation.screens.core.dialog.MediaListDialog
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.components.RotatingHourGlass
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.MediaListView
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.ProfileList
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.TrendingtopCarousel
import com.ezhilan.cine.presentation.util.enableGesture

@Composable
fun TrendingDestination(
    modifier: Modifier = Modifier,
    viewModel: TrendingScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Box(modifier = modifier.fillMaxSize()) {
        PullToRefreshContainer(
            modifier = modifier.fillMaxSize(),
            isLoading = uiState.isLoading,
            onRefresh = { viewModel.onUiEvent(TrendingScreenUiEvent.OnRefresh) },
        ) {
            TrendingScreen(
                modifier = modifier,
                uiState = uiState,
                uiEvent = viewModel::onUiEvent,
            )
        }

        if (uiState.isLoading) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.75f))
            ) {
                RotatingHourGlass(
                    modifier = modifier
                        .align(Alignment.Center)
                        .size(MaterialTheme.spacing.grid5)
                )
            }
        }
    }

    if (uiState.screenStack.contains(TrendingNavigationItem.VIEW_ALL_LIST)) {
        MediaListDialog(
            isLoading = uiState.isLoading,
            title = when (uiState.selectedMediaType) {
                MediaType.all -> "All trending"
                MediaType.movie -> "Trending Movies"
                MediaType.tv -> "Trending TV Shows"
                MediaType.person -> "Trending People"
            },
            list = when (uiState.selectedMediaType) {
                MediaType.all -> uiState.allTrendingList
                MediaType.movie -> uiState.trendingMovieList
                MediaType.tv -> uiState.trendingTvList
                MediaType.person -> uiState.trendingPeopleList
            },
            mediaType = uiState.selectedMediaType,
            onLoadMore = { viewModel.onUiEvent(TrendingScreenUiEvent.OnLoadMore) },
            onDismiss = { viewModel.onUiEvent(TrendingScreenUiEvent.Dismiss) },
        )
    }
}

@Composable
fun TrendingScreen(
    modifier: Modifier = Modifier,
    uiState: TrendingScreenUiState,
    uiEvent: (TrendingScreenUiEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier.enableGesture(!uiState.isLoading),
        topBar = {
            TopAppBar(title = {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        modifier = modifier.height(IntrinsicSize.Max),
                        text = "Trending",
                    )
                    Spacer(modifier = modifier.width(MaterialTheme.spacing.grid1))
                    ExposedDropdownMenuBox(
                        modifier = modifier.padding(bottom = MaterialTheme.spacing.unit4),
                        expanded = uiState.screenStack.contains(TrendingNavigationItem.TIME_WINDOW_DD),
                        onExpandedChange = {
                            if (!it) {
                                uiEvent(TrendingScreenUiEvent.Dismiss)
                            }
                        },
                    ) {
                        FilledTonalButton(
                            modifier = modifier
                                .menuAnchor()
                                .height(MaterialTheme.spacing.grid2),
                            onClick = { uiEvent(TrendingScreenUiEvent.ShowTimeWindowDD) },
                            contentPadding = PaddingValues(0.dp),
                            shape = MaterialTheme.shapes.extraSmall,
                        ) {
                            Spacer(modifier = modifier.width(MaterialTheme.spacing.unit4))
                            Text(
                                text = uiState.timeWindowList[uiState.selectedTimeWindowIndex],
                                style = MaterialTheme.textStyle.trendingScreenTitleSmall,
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                            )
                        }
                        DropdownMenu(
                            expanded = uiState.screenStack.contains(TrendingNavigationItem.TIME_WINDOW_DD),
                            onDismissRequest = { uiEvent(TrendingScreenUiEvent.Dismiss) },
                        ) {
                            listOf("Today", "This week").forEachIndexed { index, item ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            Text(text = item)
                                            if (uiState.selectedTimeWindowIndex == index) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_check),
                                                    contentDescription = null,
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        uiEvent(
                                            TrendingScreenUiEvent.OnTimeWindowSelected(index)
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            })
        },
    ) { safeAreaPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(safeAreaPadding),
        ) {
            if (uiState.allTrendingList.isNotEmpty()) {
                TrendingtopCarousel(
                    trendingList = uiState.allTrendingList,
                    onViewAllClick = { uiEvent(TrendingScreenUiEvent.OnViewAllPressed(MediaType.all)) },
                )
                Spacer(modifier = modifier.height(MaterialTheme.spacing.grid1))
            }
            if (uiState.trendingMovieList.isNotEmpty()) {
                HorizontalListContainer(
                    label = "Movie",
                    onViewAllClick = { uiEvent(TrendingScreenUiEvent.OnViewAllPressed(MediaType.movie)) },
                    listView = { MediaListView(mediaList = uiState.trendingMovieList) },
                )
            }
            if (uiState.trendingTvList.isNotEmpty()) {
                HorizontalListContainer(
                    label = "TV Shows",
                    onViewAllClick = { uiEvent(TrendingScreenUiEvent.OnViewAllPressed(MediaType.tv)) },
                    listView = { MediaListView(mediaList = uiState.trendingTvList) },
                )
            }
            if (uiState.trendingPeopleList.isNotEmpty()) {
                HorizontalListContainer(
                    label = "People",
                    onViewAllClick = { uiEvent(TrendingScreenUiEvent.OnViewAllPressed(MediaType.person)) },
                    listView = { ProfileList(trendingList = uiState.trendingPeopleList) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun TrendingScreenPreview() {
    CineTheme {
        TrendingScreen(
            uiState = TrendingScreenUiState(
                isLoading = true,
            ),
            uiEvent = {},
        )
    }
}