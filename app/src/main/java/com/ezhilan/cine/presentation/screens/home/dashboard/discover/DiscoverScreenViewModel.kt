package com.ezhilan.cine.presentation.screens.home.dashboard.discover

import androidx.lifecycle.viewModelScope
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.useCases.home.GetMovieListUseCase
import com.ezhilan.cine.domain.useCases.home.MovieListType
import com.ezhilan.cine.presentation.screens.core.ScreenUiState
import com.ezhilan.cine.presentation.screens.core.ScreenViewModel
import com.ezhilan.cine.presentation.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class DiscoverNavigationItem {
    ALERT, VIEW_ALL_LIST
}

data class DiscoverScreenUiState(
    override val isLoading: Boolean = true,
    override val screenStack: List<DiscoverNavigationItem> = listOf(),
    override val alertMessage: UiText = UiText.Value(),
    val searchQuery: String = "",
    val nowPlayingMovies: List<MediaData> = emptyList(),
    val popularMovies: List<MediaData> = emptyList(),
    val topRatedMovies: List<MediaData> = emptyList(),
    val upcomingMovies: List<MediaData> = emptyList(),
    val selectedMovieListType: MovieListType = MovieListType.now_playing,
) : ScreenUiState {
    override fun copyWith(isLoading: Boolean): ScreenUiState = copy(isLoading = isLoading)
}

sealed class DiscoverScreenUiEvent {
    data class OnSearchQueryChanged(val query: String) : DiscoverScreenUiEvent()
    data object OnRefresh : DiscoverScreenUiEvent()
    data object OnLoadMore : DiscoverScreenUiEvent()
    data object OnNowPlayingViewAllClicked : DiscoverScreenUiEvent()
    data object OnPopularViewAllClicked : DiscoverScreenUiEvent()
    data object OnTopRatedViewAllClicked : DiscoverScreenUiEvent()
    data object OnUpcomingViewAllClicked : DiscoverScreenUiEvent()

    data object OnDismiss : DiscoverScreenUiEvent()
}

@HiltViewModel
class DiscoverScreenViewModel @Inject constructor(
    private val getMovieList: GetMovieListUseCase,
) : ScreenViewModel<DiscoverScreenUiState, DiscoverScreenUiEvent>(DiscoverScreenUiState()) {

    init {
        refreshAllList()
    }

    private fun refreshAllList() {
        viewModelScope.launch {
            refreshMovieList(movieListType = MovieListType.now_playing)
            refreshMovieList(movieListType = MovieListType.popular)
            refreshMovieList(movieListType = MovieListType.top_rated)
            refreshMovieList(movieListType = MovieListType.upcoming)
        }
    }

    private suspend fun refreshMovieList(
        isPagingEnabled: Boolean = false, movieListType: MovieListType
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

    override fun onUiEvent(event: DiscoverScreenUiEvent) {
        when (event) {
            DiscoverScreenUiEvent.OnRefresh -> refreshAllList()
            DiscoverScreenUiEvent.OnLoadMore -> viewModelScope.launch {
                refreshMovieList(
                    isPagingEnabled = true, movieListType = uiState.value.selectedMovieListType
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

            DiscoverScreenUiEvent.OnDismiss -> updateUiState { currentState ->
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