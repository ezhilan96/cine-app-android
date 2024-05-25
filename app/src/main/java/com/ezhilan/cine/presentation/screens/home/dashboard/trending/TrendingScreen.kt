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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ezhilan.cine.R
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.domain.entity.TrendingData
import com.ezhilan.cine.presentation.config.CineTheme
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.config.textStyle
import com.ezhilan.cine.presentation.screens.core.component.PullToRefreshContainer
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.components.RotatingHourGlass
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.FullScreenCarousel
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.MediaList
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.ProfileList
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.TrendingListDialog
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
        TrendingListDialog(
            uiState = uiState,
            uiEvent = viewModel::onUiEvent,
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
                    Spacer(modifier = modifier.width(MaterialTheme.spacing.unit4))
                    ExposedDropdownMenuBox(
                        modifier = modifier.padding(bottom = MaterialTheme.spacing.grid05),
                        expanded = uiState.screenStack.contains(TrendingNavigationItem.TIME_WINDOW_DD),
                        onExpandedChange = {
                            if (!it) {
                                uiEvent(TrendingScreenUiEvent.Dismiss)
                            }
                        },
                    ) {
                        TextButton(
                            modifier = modifier
                                .menuAnchor()
                                .height(MaterialTheme.spacing.grid2),
                            onClick = { uiEvent(TrendingScreenUiEvent.ShowTimeWindowDD) },
                            contentPadding = PaddingValues(0.dp),
                            shape = MaterialTheme.shapes.extraSmall,
                        ) {
                            Text("[${uiState.timeWindowList[uiState.selectedTimeWindowIndex]}]")
                        }
                        DropdownMenu(
                            expanded = uiState.screenStack.contains(TrendingNavigationItem.TIME_WINDOW_DD),
                            onDismissRequest = { uiEvent(TrendingScreenUiEvent.Dismiss) },
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row {
                                        Text(text = "Today")
                                        if (uiState.selectedTimeWindowIndex == 0) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_check),
                                                contentDescription = null,
                                            )
                                        }
                                    }
                                },
                                onClick = { uiEvent(TrendingScreenUiEvent.OnTimeWindowSelected(0)) },
                            )
                            DropdownMenuItem(
                                text = {
                                    Row {
                                        Text(text = "This week")
                                        if (uiState.selectedTimeWindowIndex == 1) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_check),
                                                contentDescription = null,
                                            )
                                        }
                                    }
                                },
                                onClick = { uiEvent(TrendingScreenUiEvent.OnTimeWindowSelected(1)) },
                            )
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
                FullScreenCarousel(
                    trendingList = uiState.allTrendingList,
                    onViewAllClick = { uiEvent(TrendingScreenUiEvent.OnViewAllPressed(MediaType.all)) },
                )
            }
            if (uiState.trendingMovieList.isNotEmpty()) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = MaterialTheme.spacing.grid1),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "Movies", style = MaterialTheme.textStyle.dashboardTitle)
                    TextButton(onClick = { uiEvent(TrendingScreenUiEvent.OnViewAllPressed(MediaType.movie)) }) {
                        Text(
                            text = "View all",
                            style = LocalTextStyle.current.copy(textDecoration = TextDecoration.Underline),
                        )
                        Spacer(modifier = modifier.width(MaterialTheme.spacing.grid05))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_next),
                            contentDescription = null,
                        )
                    }
                }
                MediaList(trendingList = uiState.trendingMovieList)
            }
            if (uiState.trendingTvList.isNotEmpty()) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = MaterialTheme.spacing.grid1),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "TV Shows", style = MaterialTheme.textStyle.dashboardTitle)
                    TextButton(onClick = { uiEvent(TrendingScreenUiEvent.OnViewAllPressed(MediaType.tv)) }) {
                        Text(
                            text = "View all",
                            style = LocalTextStyle.current.copy(textDecoration = TextDecoration.Underline),
                        )
                        Spacer(modifier = modifier.width(MaterialTheme.spacing.grid05))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_next),
                            contentDescription = null,
                        )
                    }
                }
                MediaList(trendingList = uiState.trendingTvList)
            }
            if (uiState.trendingPeopleList.isNotEmpty()) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = MaterialTheme.spacing.grid1),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "People", style = MaterialTheme.textStyle.dashboardTitle)
                    TextButton(onClick = { uiEvent(TrendingScreenUiEvent.OnViewAllPressed(MediaType.person)) }) {
                        Text(
                            text = "View all",
                            style = LocalTextStyle.current.copy(textDecoration = TextDecoration.Underline),
                        )
                        Spacer(modifier = modifier.width(MaterialTheme.spacing.grid05))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_next),
                            contentDescription = null,
                        )
                    }
                }
                ProfileList(trendingList = uiState.trendingPeopleList)
            }
            Spacer(modifier = modifier.height(MaterialTheme.spacing.grid1))
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
                allTrendingList = listOf(
                    TrendingData(
                        id = "",
                        title = "Title",
                        overview = "Overview",
                        backdropPath = "/u1CqlLecfpcuOaugKi3ol9gDQHJ.jpg",
                        posterPath = "/vlHJfLsduZiILN8eYdN57kHZTcQ.jpg",
                        releaseYear = "2022",
                        mediaType = MediaType.tv,
                        genres = listOf(),
                        rating = "7.8",
                    )
                ),
                trendingMovieList = listOf(
                    TrendingData(
                        id = "",
                        title = "Title",
                        overview = "Overview",
                        backdropPath = "/u1CqlLecfpcuOaugKi3ol9gDQHJ.jpg",
                        posterPath = "/vlHJfLsduZiILN8eYdN57kHZTcQ.jpg",
                        releaseYear = "2022",
                        mediaType = MediaType.tv,
                        genres = listOf(),
                        rating = "7.8",
                    )
                ),
                trendingTvList = listOf(
                    TrendingData(
                        id = "",
                        title = "Title",
                        overview = "Overview",
                        backdropPath = "/u1CqlLecfpcuOaugKi3ol9gDQHJ.jpg",
                        posterPath = "/vlHJfLsduZiILN8eYdN57kHZTcQ.jpg",
                        releaseYear = "2022",
                        mediaType = MediaType.tv,
                        genres = listOf(),
                        rating = "7.8",
                    )
                ),
            ),
            uiEvent = {},
        )
    }
}