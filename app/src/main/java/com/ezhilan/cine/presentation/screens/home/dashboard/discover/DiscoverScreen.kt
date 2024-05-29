package com.ezhilan.cine.presentation.screens.home.dashboard.discover

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.domain.useCases.home.MovieListType
import com.ezhilan.cine.presentation.config.CineTheme
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.screens.core.component.HorizontalListContainer
import com.ezhilan.cine.presentation.screens.core.component.PullToRefreshContainer
import com.ezhilan.cine.presentation.screens.core.component.TopBarWithSearchBar
import com.ezhilan.cine.presentation.screens.core.dialog.MediaListDialog
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.components.RotatingHourGlass
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.view.MediaListView

@Composable
fun DiscoverDestination(
    modifier: Modifier = Modifier,
    viewModel: DiscoverScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Box(modifier = modifier.fillMaxSize()) {
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

    when {
        uiState.screenStack.contains(DiscoverNavigationItem.VIEW_ALL_LIST) -> {
            MediaListDialog(
                isLoading = uiState.isLoading,
                mediaType = MediaType.movie,
                title = when (uiState.selectedMovieListType) {
                    MovieListType.now_playing -> "Now playing"
                    MovieListType.popular -> "Popular"
                    MovieListType.top_rated -> "Top rated"
                    MovieListType.upcoming -> "Upcoming"
                },
                list = when (uiState.selectedMovieListType) {
                    MovieListType.now_playing -> uiState.nowPlayingMovies
                    MovieListType.popular -> uiState.popularMovies
                    MovieListType.top_rated -> uiState.topRatedMovies
                    MovieListType.upcoming -> uiState.upcomingMovies
                },
                onLoadMore = { viewModel.onUiEvent(DiscoverScreenUiEvent.OnLoadMore) },
                onDismiss = { viewModel.onUiEvent(DiscoverScreenUiEvent.OnDismiss) },
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
            TopBarWithSearchBar(
                query = "", onQueryChanged = {},
                title = {
                    Text(text = "Discover")
                },
            )
        },
    ) { safeAreaPadding ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(safeAreaPadding)
        ) {
            if (uiState.nowPlayingMovies.isNotEmpty()) {
                HorizontalListContainer(
                    label = "Now playing",
                    onViewAllClick = { uiEvent(DiscoverScreenUiEvent.OnNowPlayingViewAllClicked) },
                    listView = {
                        MediaListView(mediaList = uiState.nowPlayingMovies)
                    },
                )
            }
            if (uiState.popularMovies.isNotEmpty()) {
                HorizontalListContainer(
                    label = "Popular",
                    onViewAllClick = { uiEvent(DiscoverScreenUiEvent.OnPopularViewAllClicked) },
                    listView = {
                        MediaListView(mediaList = uiState.popularMovies)
                    },
                )
            }
            if (uiState.topRatedMovies.isNotEmpty()) {
                HorizontalListContainer(
                    label = "Top rated",
                    onViewAllClick = { uiEvent(DiscoverScreenUiEvent.OnTopRatedViewAllClicked) },
                    listView = {
                        MediaListView(mediaList = uiState.topRatedMovies)
                    },
                )
            }
            if (uiState.upcomingMovies.isNotEmpty()) {
                HorizontalListContainer(
                    label = "Upcoming",
                    onViewAllClick = { uiEvent(DiscoverScreenUiEvent.OnUpcomingViewAllClicked) },
                    listView = {
                        MediaListView(mediaList = uiState.upcomingMovies)
                    },
                )
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