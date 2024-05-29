package com.ezhilan.cine.presentation.screens.home.dashboard.discover

import androidx.lifecycle.viewModelScope
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.domain.useCases.home.GetMovieListUseCase
import com.ezhilan.cine.domain.useCases.home.GetTVListUseCase
import com.ezhilan.cine.domain.useCases.home.MovieListType
import com.ezhilan.cine.domain.useCases.home.TvListType
import com.ezhilan.cine.presentation.screens.core.ScreenUiState
import com.ezhilan.cine.presentation.screens.core.ScreenViewModel
import com.ezhilan.cine.presentation.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class DiscoverNavigationItem {
    ALERT, VIEW_ALL_LIST, MEDIA_TYPE_DD
}

data class DiscoverScreenUiState(
    override val isLoading: Boolean = true,
    override val screenStack: List<DiscoverNavigationItem> = listOf(),
    override val alertMessage: UiText = UiText.Value(),
    val searchQuery: String = "",
    val mediaTypeList: List<MediaType> = MediaType.entries.filterNot { it == MediaType.all },
    val selectedMediaTypeIndex: Int = 0,
    val nowPlayingMovies: List<MediaData> = emptyList(),
    val popularMovies: List<MediaData> = emptyList(),
    val topRatedMovies: List<MediaData> = emptyList(),
    val upcomingMovies: List<MediaData> = emptyList(),
    val airingTodayTvShows: List<MediaData> = emptyList(),
    val onTheAirTvShows: List<MediaData> = emptyList(),
    val popularTvShows: List<MediaData> = emptyList(),
    val topRatedTvShows: List<MediaData> = emptyList(),
    val selectedMovieListType: MovieListType = MovieListType.now_playing,
    val selectedTvListType: TvListType = TvListType.airing_today,
) : ScreenUiState {
    override fun copyWith(isLoading: Boolean): ScreenUiState = copy(isLoading = isLoading)
}

sealed class DiscoverScreenUiEvent {
    data class OnSearchQueryChanged(val query: String) : DiscoverScreenUiEvent()
    data object OnRefresh : DiscoverScreenUiEvent()
    data object OnLoadMore : DiscoverScreenUiEvent()
    data object OnMediaTypeClicked : DiscoverScreenUiEvent()
    data class OnMediaTypeSelected(val index: Int) : DiscoverScreenUiEvent()
    data object OnNowPlayingViewAllClicked : DiscoverScreenUiEvent()
    data object OnPopularViewAllClicked : DiscoverScreenUiEvent()
    data object OnTopRatedViewAllClicked : DiscoverScreenUiEvent()
    data object OnUpcomingViewAllClicked : DiscoverScreenUiEvent()
    data object OnAiringTodayViewAllClicked : DiscoverScreenUiEvent()
    data object OnOnTheAirViewAllClicked : DiscoverScreenUiEvent()
    data object OnPopularTvViewAllClicked : DiscoverScreenUiEvent()
    data object OnTopRatedTvViewAllClicked : DiscoverScreenUiEvent()
    data object Dismiss : DiscoverScreenUiEvent()
}

@HiltViewModel
class DiscoverScreenViewModel @Inject constructor(
    private val getMovieList: GetMovieListUseCase,
    private val getTvList: GetTVListUseCase,
) : ScreenViewModel<DiscoverScreenUiState, DiscoverScreenUiEvent>(DiscoverScreenUiState()) {

    private fun refreshAllList() {
        when (uiState.value.mediaTypeList[uiState.value.selectedMediaTypeIndex]) {
            MediaType.movie -> viewModelScope.launch {
                refreshMovieList(movieListType = MovieListType.now_playing)
                refreshMovieList(movieListType = MovieListType.popular)
                refreshMovieList(movieListType = MovieListType.top_rated)
                refreshMovieList(movieListType = MovieListType.upcoming)
            }

            MediaType.tv -> {
                viewModelScope.launch {
                    refreshTvList(tvListType = TvListType.airing_today)
                    refreshTvList(tvListType = TvListType.on_the_air)
                    refreshTvList(tvListType = TvListType.popular)
                    refreshTvList(tvListType = TvListType.top_rated)
                }
            }

            MediaType.person -> {}
            else -> {}
        }
    }

    private suspend fun refreshMovieList(
        isPagingEnabled: Boolean = false,
        movieListType: MovieListType,
    ) {
        getMovieList(
            pagingEnabled = isPagingEnabled,
            movieListType = movieListType,
        ).collectDataState { dataState ->
            updateUiState { currentState ->
                when (movieListType) {
                    MovieListType.now_playing -> currentState.copy(
                        nowPlayingMovies = mutableListOf<MediaData>().apply { addAll(dataState.data) },
                    )

                    MovieListType.popular -> currentState.copy(
                        popularMovies = mutableListOf<MediaData>().apply { addAll(dataState.data) },
                    )

                    MovieListType.top_rated -> currentState.copy(
                        topRatedMovies = mutableListOf<MediaData>().apply { addAll(dataState.data) },
                    )

                    MovieListType.upcoming -> currentState.copy(
                        upcomingMovies = mutableListOf<MediaData>().apply { addAll(dataState.data) },
                    )
                }
            }
        }
    }

    private suspend fun refreshTvList(
        isPagingEnabled: Boolean = false,
        tvListType: TvListType,
    ) {
        getTvList(
            pagingEnabled = isPagingEnabled,
            tvListType = tvListType,
        ).collectDataState { dataState ->
            updateUiState { currentState ->
                when (tvListType) {
                    TvListType.airing_today -> currentState.copy(
                        airingTodayTvShows = mutableListOf<MediaData>().apply { addAll(dataState.data) },
                    )

                    TvListType.on_the_air -> currentState.copy(
                        onTheAirTvShows = mutableListOf<MediaData>().apply { addAll(dataState.data) },
                    )

                    TvListType.popular -> currentState.copy(
                        popularTvShows = mutableListOf<MediaData>().apply { addAll(dataState.data) },
                    )

                    TvListType.top_rated -> currentState.copy(
                        topRatedTvShows = mutableListOf<MediaData>().apply { addAll(dataState.data) },
                    )
                }
            }
        }
    }

    override fun onUiEvent(event: DiscoverScreenUiEvent) {
        when (event) {
            DiscoverScreenUiEvent.OnRefresh -> refreshAllList()
            DiscoverScreenUiEvent.OnLoadMore -> viewModelScope.launch {
                when (uiState.value.mediaTypeList[uiState.value.selectedMediaTypeIndex]) {
                    MediaType.movie -> refreshMovieList(
                        isPagingEnabled = true,
                        movieListType = uiState.value.selectedMovieListType,
                    )

                    MediaType.tv -> refreshTvList(
                        isPagingEnabled = true,
                        tvListType = uiState.value.selectedTvListType,
                    )

                    else -> {}
                }
            }

            DiscoverScreenUiEvent.OnMediaTypeClicked -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(DiscoverNavigationItem.MEDIA_TYPE_DD)
                    },
                )
            }

            is DiscoverScreenUiEvent.OnMediaTypeSelected -> updateUiState { currentState ->
                currentState.copy(
                    selectedMediaTypeIndex = event.index,
                    screenStack = currentState.screenStack.toMutableList().apply {
                        remove(DiscoverNavigationItem.MEDIA_TYPE_DD)
                    },
                )
            }

            is DiscoverScreenUiEvent.OnSearchQueryChanged -> updateUiState { currentState ->
                currentState.copy(searchQuery = event.query)
            }

            DiscoverScreenUiEvent.OnNowPlayingViewAllClicked -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(DiscoverNavigationItem.VIEW_ALL_LIST)
                    },
                    selectedMovieListType = MovieListType.now_playing,
                )
            }

            DiscoverScreenUiEvent.OnPopularViewAllClicked -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(DiscoverNavigationItem.VIEW_ALL_LIST)
                    },
                    selectedMovieListType = MovieListType.popular,
                )
            }

            DiscoverScreenUiEvent.OnTopRatedViewAllClicked -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(DiscoverNavigationItem.VIEW_ALL_LIST)
                    },
                    selectedMovieListType = MovieListType.top_rated,
                )
            }

            DiscoverScreenUiEvent.OnUpcomingViewAllClicked -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(DiscoverNavigationItem.VIEW_ALL_LIST)
                    },
                    selectedMovieListType = MovieListType.upcoming,
                )
            }

            DiscoverScreenUiEvent.OnAiringTodayViewAllClicked -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(DiscoverNavigationItem.VIEW_ALL_LIST)
                    },
                    selectedTvListType = TvListType.airing_today,
                )
            }

            DiscoverScreenUiEvent.OnOnTheAirViewAllClicked -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(DiscoverNavigationItem.VIEW_ALL_LIST)
                    },
                    selectedTvListType = TvListType.on_the_air,
                )
            }

            DiscoverScreenUiEvent.OnPopularTvViewAllClicked -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(DiscoverNavigationItem.VIEW_ALL_LIST)
                    },
                    selectedTvListType = TvListType.popular,
                )
            }

            DiscoverScreenUiEvent.OnTopRatedTvViewAllClicked -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(DiscoverNavigationItem.VIEW_ALL_LIST)
                    },
                    selectedTvListType = TvListType.top_rated,
                )
            }

            DiscoverScreenUiEvent.Dismiss -> updateUiState { currentState ->
                currentState.copy(screenStack = listOf())
            }
        }
    }

    override fun showAlert(message: UiText) {
        updateUiState { currentState ->
            currentState.copy(
                screenStack = currentState.screenStack.toMutableList().apply {
                    add(DiscoverNavigationItem.ALERT)
                },
                alertMessage = message,
            )
        }
    }

    override fun onStop() {
        updateUiState { currentState ->
            currentState.copy(screenStack = listOf())
        }
    }

}