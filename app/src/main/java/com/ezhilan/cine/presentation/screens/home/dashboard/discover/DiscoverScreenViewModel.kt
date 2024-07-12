package com.ezhilan.cine.presentation.screens.home.dashboard.discover

import androidx.lifecycle.viewModelScope
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.domain.useCases.home.discoverList.GetMovieListUseCase
import com.ezhilan.cine.domain.useCases.home.discoverList.GetPeopleListUseCase
import com.ezhilan.cine.domain.useCases.home.discoverList.GetTVListUseCase
import com.ezhilan.cine.domain.useCases.home.discoverList.MovieListType
import com.ezhilan.cine.domain.useCases.home.discoverList.TvListType
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
    val selectedMediaType: MediaType = MediaType.all,

    val nowPlayingMovies: List<MediaData> = emptyList(),
    val popularMovies: List<MediaData> = emptyList(),
    val topRatedMovies: List<MediaData> = emptyList(),
    val upcomingMovies: List<MediaData> = emptyList(),
    val airingTodayTvShows: List<MediaData> = emptyList(),
    val onTheAirTvShows: List<MediaData> = emptyList(),
    val popularTvShows: List<MediaData> = emptyList(),
    val topRatedTvShows: List<MediaData> = emptyList(),
    val popularPeople: List<MediaData> = emptyList(),

    val viewAllDialogTitle: String = "",
    val viewAllDialogList: List<MediaData> = emptyList(),
    val viewAllDialogMediaType: MediaType = MediaType.movie,
    val viewAllDialogMovieListType: MovieListType? = null,
    val viewAllDialogTvListType: TvListType? = null,
) : ScreenUiState {
    override fun copyWith(isLoading: Boolean): ScreenUiState = copy(isLoading = isLoading)
}

sealed class DiscoverScreenUiEvent {
    data class OnSearchQueryChanged(val query: String) : DiscoverScreenUiEvent()
    data object OnRefresh : DiscoverScreenUiEvent()
    data object OnLoadMore : DiscoverScreenUiEvent()
    data object OnMediaTypeClicked : DiscoverScreenUiEvent()
    data class OnMediaTypeSelected(val mediaType: MediaType) : DiscoverScreenUiEvent()
    data class OnViewAllClicked(
        val movieListType: MovieListType? = null, val tvListType: TvListType? = null
    ) : DiscoverScreenUiEvent()

    data object Dismiss : DiscoverScreenUiEvent()
}

@HiltViewModel
class DiscoverScreenViewModel @Inject constructor(
    private val getMovieList: GetMovieListUseCase,
    private val getTvList: GetTVListUseCase,
    private val getPeopleList: GetPeopleListUseCase,
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
            refreshTvList(tvListType = TvListType.airing_today)
            refreshTvList(tvListType = TvListType.on_the_air)
            refreshTvList(tvListType = TvListType.popular)
            refreshTvList(tvListType = TvListType.top_rated)
            refreshPeopleList()
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

    private fun refreshPeopleList(isPagingEnabled: Boolean = false) {
        viewModelScope.launch {
            getPeopleList(
                pagingEnabled = isPagingEnabled,
            ).collectDataState { dataState ->
                updateUiState { currentState ->
                    currentState.copy(
                        popularPeople = mutableListOf<MediaData>().apply { addAll(dataState.data) },
                    )
                }
            }
        }
    }

    override fun onUiEvent(event: DiscoverScreenUiEvent) {
        when (event) {
            DiscoverScreenUiEvent.OnRefresh -> refreshAllList()
            DiscoverScreenUiEvent.OnLoadMore -> viewModelScope.launch {
                when (uiState.value.viewAllDialogMediaType) {
                    MediaType.movie -> refreshMovieList(
                        isPagingEnabled = true,
                        movieListType = uiState.value.viewAllDialogMovieListType!!,
                    )

                    MediaType.tv -> refreshTvList(
                        isPagingEnabled = true,
                        tvListType = uiState.value.viewAllDialogTvListType!!,
                    )

                    MediaType.person -> refreshPeopleList(isPagingEnabled = true)

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
                    selectedMediaType = event.mediaType,
                    screenStack = currentState.screenStack.toMutableList().apply {
                        remove(DiscoverNavigationItem.MEDIA_TYPE_DD)
                    },
                )
            }

            is DiscoverScreenUiEvent.OnSearchQueryChanged -> updateUiState { currentState ->
                currentState.copy(searchQuery = event.query)
            }

            is DiscoverScreenUiEvent.OnViewAllClicked -> {
                val currentMediaType = when {
                    event.movieListType != null -> MediaType.movie
                    event.tvListType != null -> MediaType.tv
                    else -> MediaType.person
                }
                updateUiState { currentState ->
                    currentState.copy(
                        screenStack = currentState.screenStack.toMutableList().apply {
                            add(DiscoverNavigationItem.VIEW_ALL_LIST)
                        },
                        viewAllDialogMediaType = currentMediaType,
                        viewAllDialogMovieListType = event.movieListType,
                        viewAllDialogTvListType = event.tvListType,
                        viewAllDialogTitle = when (currentMediaType) {
                            MediaType.movie -> when (event.movieListType!!) {
                                MovieListType.now_playing -> "Movies Now playing"
                                MovieListType.popular -> "Popular movies"
                                MovieListType.top_rated -> "Top rated movies"
                                MovieListType.upcoming -> "Upcoming movies"
                            }

                            MediaType.tv -> when (event.tvListType!!) {
                                TvListType.airing_today -> "Tv shows Airing today"
                                TvListType.on_the_air -> "Tv shows On the air"
                                TvListType.popular -> "Popular tv shows"
                                TvListType.top_rated -> "Top rated tv shows"
                            }

                            else -> "Popular people"
                        },
                        viewAllDialogList = when (currentMediaType) {
                            MediaType.movie -> when (event.movieListType!!) {
                                MovieListType.now_playing -> uiState.value.nowPlayingMovies
                                MovieListType.popular -> uiState.value.popularMovies
                                MovieListType.top_rated -> uiState.value.topRatedMovies
                                MovieListType.upcoming -> uiState.value.upcomingMovies
                            }

                            MediaType.tv -> when (event.tvListType!!) {
                                TvListType.airing_today -> uiState.value.airingTodayTvShows
                                TvListType.on_the_air -> uiState.value.onTheAirTvShows
                                TvListType.popular -> uiState.value.popularTvShows
                                TvListType.top_rated -> uiState.value.topRatedTvShows
                            }

                            else -> uiState.value.popularPeople
                        }
                    )
                }
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