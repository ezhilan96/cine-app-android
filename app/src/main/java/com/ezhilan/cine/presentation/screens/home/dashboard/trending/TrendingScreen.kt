@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.ezhilan.cine.presentation.screens.home.dashboard.trending

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.FullScreenCarousel
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.MediaList

@Composable
fun TrendingDestination(
    modifier: Modifier = Modifier,
    viewModel: TrendingScreenViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    TrendingScreen(
        modifier = modifier,
        uiState = uiState.value,
        uiEvent = viewModel::onUiEvent,
    )
}

@Composable
fun TrendingScreen(
    modifier: Modifier = Modifier,
    uiState: TrendingScreenUiState,
    uiEvent: (TrendingScreenUiEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
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
                },
                actions = {
                    ExposedDropdownMenuBox(
                        expanded = uiState.screenStack.contains(TrendingNavigationItem.LANGUAGE_DD),
                        onExpandedChange = {
                            if (!it) {
                                uiEvent(TrendingScreenUiEvent.Dismiss)
                            }
                        },
                    ) {
                        IconButton(modifier = modifier.menuAnchor(),
                            onClick = { uiEvent(TrendingScreenUiEvent.ShowLanguageDD) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_globe),
                                contentDescription = null,
                            )
                        }
                        DropdownMenu(
                            expanded = uiState.screenStack.contains(TrendingNavigationItem.LANGUAGE_DD),
                            onDismissRequest = { uiEvent(TrendingScreenUiEvent.Dismiss) },
                        ) {
                            uiState.languageList.forEachIndexed { index, language ->
                                DropdownMenuItem(
                                    text = {
                                        Row {
                                            Text(text = language)
                                            if (uiState.selectedLanguageIndex == index) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_check),
                                                    contentDescription = null,
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        uiEvent(TrendingScreenUiEvent.OnLanguageSelected(index))
                                    },
                                )

                            }
                        }
                    }
                },
            )
        },
    ) { safeAreaPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(safeAreaPadding),
        ) {
            FullScreenCarousel(trendingList = uiState.allTrendingList)
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.spacing.grid1),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Movies", style = MaterialTheme.textStyle.dashboardTitle)
                TextButton(onClick = { /*TODO*/ }) {
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
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.spacing.grid1),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Tv", style = MaterialTheme.textStyle.dashboardTitle)
                TextButton(onClick = { /*TODO*/ }) {
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
    }
}

@Preview
@Composable
private fun TrendingScreenPreview() {
    CineTheme {
        TrendingScreen(
            uiState = TrendingScreenUiState(
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