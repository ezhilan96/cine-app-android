@file:OptIn(ExperimentalMaterial3Api::class)

package com.ezhilan.cine.presentation.screens.home.dashboard.discover

import android.annotation.SuppressLint
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
import com.ezhilan.cine.domain.useCases.home.MovieListType
import com.ezhilan.cine.domain.useCases.home.TvListType
import com.ezhilan.cine.presentation.config.CineTheme
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.config.textStyle
import com.ezhilan.cine.presentation.screens.core.component.HorizontalListContainer
import com.ezhilan.cine.presentation.screens.core.component.PullToRefreshContainer
import com.ezhilan.cine.presentation.screens.core.component.TopBarWithSearchBar
import com.ezhilan.cine.presentation.screens.core.component.TopProgressIndicator
import com.ezhilan.cine.presentation.screens.core.dialog.MediaListDialog
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.MediaListView
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.ProfileList

@Composable
fun DiscoverDestination(
    modifier: Modifier = Modifier,
    viewModel: DiscoverScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PullToRefreshContainer(
        modifier = modifier.fillMaxSize(),
        isLoading = uiState.isLoading,
        onRefresh = { viewModel.onUiEvent(DiscoverScreenUiEvent.OnRefresh) },
    ) {
        DiscoverScreen(
            modifier = modifier,
            uiState = uiState,
            uiEvent = viewModel::onUiEvent,
        )
    }

    when {
        uiState.screenStack.contains(DiscoverNavigationItem.VIEW_ALL_LIST) -> {
            MediaListDialog(
                isLoading = uiState.isLoading,
                mediaType = uiState.viewAllDialogMediaType,
                title = uiState.viewAllDialogTitle,
                list = uiState.viewAllDialogList,
                onLoadMore = { viewModel.onUiEvent(DiscoverScreenUiEvent.OnLoadMore) },
                onDismiss = { viewModel.onUiEvent(DiscoverScreenUiEvent.Dismiss) },
            )
        }
    }
}

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    uiState: DiscoverScreenUiState,
    uiEvent: (DiscoverScreenUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Column {
                TopBarWithSearchBar(
                    query = "", onQueryChanged = {},
                    title = {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                modifier = modifier.height(IntrinsicSize.Max),
                                text = "Discover",
                            )
                            Spacer(modifier = modifier.width(MaterialTheme.spacing.grid1))
                            ExposedDropdownMenuBox(
                                modifier = modifier.padding(bottom = MaterialTheme.spacing.unit4),
                                expanded = uiState.screenStack.contains(DiscoverNavigationItem.MEDIA_TYPE_DD),
                                onExpandedChange = {
                                    if (!it) {
                                        uiEvent(DiscoverScreenUiEvent.Dismiss)
                                    }
                                },
                            ) {
                                FilledTonalButton(
                                    modifier = modifier
                                        .menuAnchor()
                                        .height(MaterialTheme.spacing.grid2),
                                    onClick = { uiEvent(DiscoverScreenUiEvent.OnMediaTypeClicked) },
                                    contentPadding = PaddingValues(0.dp),
                                    shape = MaterialTheme.shapes.extraSmall,
                                ) {
                                    Spacer(modifier = modifier.width(MaterialTheme.spacing.unit4))
                                    Text(
                                        when (uiState.selectedMediaType) {
                                            MediaType.movie -> "Movies"
                                            MediaType.tv -> "TV shows"
                                            MediaType.person -> "People"
                                            MediaType.all -> "All"
                                        },
                                        style = MaterialTheme.textStyle.defaultTitleSmall,
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                    )
                                }
                                DropdownMenu(
                                    expanded = uiState.screenStack.contains(DiscoverNavigationItem.MEDIA_TYPE_DD),
                                    onDismissRequest = { uiEvent(DiscoverScreenUiEvent.Dismiss) },
                                ) {
                                    MediaType.entries.forEach { item ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(
                                                    modifier = modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                ) {
                                                    Text(
                                                        text =
                                                        when (item) {
                                                            MediaType.all -> "All"
                                                            MediaType.movie -> "Movies"
                                                            MediaType.tv -> "TV shows"
                                                            MediaType.person -> "People"
                                                        }
                                                    )
                                                    if (uiState.selectedMediaType == item) {
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.ic_check),
                                                            contentDescription = null,
                                                        )
                                                    }
                                                }
                                            },
                                            onClick = {
                                                uiEvent(
                                                    DiscoverScreenUiEvent.OnMediaTypeSelected(item)
                                                )
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    },
                )
                TopProgressIndicator(isLoading = uiState.isLoading)
            }
        },
    ) { safeAreaPadding ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(safeAreaPadding)
        ) {
            if (uiState.selectedMediaType == MediaType.movie || uiState.selectedMediaType == MediaType.all) {
                if (uiState.nowPlayingMovies.isNotEmpty()) {
                    HorizontalListContainer(
                        label = "Movies Now playing",
                        onViewAllClick = {
                            uiEvent(
                                DiscoverScreenUiEvent.OnViewAllClicked(
                                    movieListType = MovieListType.now_playing
                                )
                            )
                        },
                        listView = {
                            MediaListView(mediaList = uiState.nowPlayingMovies)
                        },
                    )
                }
                if (uiState.popularMovies.isNotEmpty()) {
                    HorizontalListContainer(
                        label = "Popular movies",
                        onViewAllClick = {
                            uiEvent(
                                DiscoverScreenUiEvent.OnViewAllClicked(
                                    movieListType = MovieListType.popular
                                )
                            )
                        },
                        listView = {
                            MediaListView(mediaList = uiState.popularMovies)
                        },
                    )
                }
                if (uiState.topRatedMovies.isNotEmpty()) {
                    HorizontalListContainer(
                        label = "Top rated movies",
                        onViewAllClick = {
                            uiEvent(
                                DiscoverScreenUiEvent.OnViewAllClicked(
                                    movieListType = MovieListType.top_rated
                                )
                            )
                        },
                        listView = {
                            MediaListView(mediaList = uiState.topRatedMovies)
                        },
                    )
                }
                if (uiState.upcomingMovies.isNotEmpty()) {
                    HorizontalListContainer(
                        label = "Upcoming movies",
                        onViewAllClick = {
                            uiEvent(
                                DiscoverScreenUiEvent.OnViewAllClicked(
                                    movieListType = MovieListType.upcoming
                                )
                            )
                        },
                        listView = {
                            MediaListView(mediaList = uiState.upcomingMovies)
                        },
                    )
                }
            }
            if (uiState.selectedMediaType == MediaType.tv || uiState.selectedMediaType == MediaType.all) {
                if (uiState.airingTodayTvShows.isNotEmpty()) {
                    HorizontalListContainer(
                        label = "TV Shows Airing today",
                        onViewAllClick = {
                            uiEvent(
                                DiscoverScreenUiEvent.OnViewAllClicked(
                                    tvListType = TvListType.airing_today
                                )
                            )
                        },
                        listView = {
                            MediaListView(mediaList = uiState.airingTodayTvShows)
                        },
                    )
                }

                if (uiState.onTheAirTvShows.isNotEmpty()) {
                    HorizontalListContainer(
                        label = "TV Shows On the air",
                        onViewAllClick = {
                            uiEvent(
                                DiscoverScreenUiEvent.OnViewAllClicked(
                                    tvListType = TvListType.on_the_air
                                )
                            )
                        },
                        listView = {
                            MediaListView(mediaList = uiState.onTheAirTvShows)
                        },
                    )
                }

                if (uiState.popularTvShows.isNotEmpty()) {
                    HorizontalListContainer(
                        label = "Popular TV Shows",
                        onViewAllClick = {
                            uiEvent(
                                DiscoverScreenUiEvent.OnViewAllClicked(
                                    tvListType = TvListType.popular
                                )
                            )
                        },
                        listView = {
                            MediaListView(mediaList = uiState.popularTvShows)
                        },
                    )
                }

                if (uiState.topRatedTvShows.isNotEmpty()) {
                    HorizontalListContainer(
                        label = "Top rated TV Shows",
                        onViewAllClick = {
                            uiEvent(
                                DiscoverScreenUiEvent.OnViewAllClicked(
                                    tvListType = TvListType.top_rated
                                )
                            )
                        },
                        listView = {
                            MediaListView(mediaList = uiState.topRatedTvShows)
                        },
                    )
                }
            }


            if (uiState.selectedMediaType == MediaType.person || uiState.selectedMediaType == MediaType.all) {
                if (uiState.popularPeople.isNotEmpty()) {
                    HorizontalListContainer(
                        label = "Popular people",
                        onViewAllClick = { uiEvent(DiscoverScreenUiEvent.OnViewAllClicked()) },
                        listView = {
                            ProfileList(peopleList = uiState.popularPeople)
                        },
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun DiscoverScreenPreview() {
    CineTheme {
        Scaffold {
            DiscoverScreen(
                uiState = DiscoverScreenUiState(),
                uiEvent = {},
            )
        }
    }
}