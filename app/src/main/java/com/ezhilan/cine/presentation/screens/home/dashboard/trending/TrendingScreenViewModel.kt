package com.ezhilan.cine.presentation.screens.home.dashboard.trending

import androidx.lifecycle.viewModelScope
import com.ezhilan.cine.data.model.remote.response.Genre
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.domain.entity.TrendingData
import com.ezhilan.cine.domain.useCases.home.GetAllTrendingUseCase
import com.ezhilan.cine.domain.useCases.home.GetTrendingMovieUseCase
import com.ezhilan.cine.domain.useCases.home.GetTrendingPeopleUseCase
import com.ezhilan.cine.domain.useCases.home.GetTrendingTvUseCase
import com.ezhilan.cine.presentation.screens.core.ScreenUiState
import com.ezhilan.cine.presentation.screens.core.ScreenViewModel
import com.ezhilan.cine.presentation.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TrendingNavigationItem {
    ALERT, TIME_WINDOW_DD, VIEW_ALL_LIST
}

val timeWindowList = listOf("day", "week")

data class TrendingScreenUiState(
    override val isLoading: Boolean = true,
    override val screenStack: List<TrendingNavigationItem> = listOf(),
    override val alertMessage: UiText = UiText.Value(),
    val timeWindowList: List<String> = listOf("Today", "This Week"),
    val selectedTimeWindowIndex: Int = 0,
    val allTrendingList: List<TrendingData> = listOf(),
    val trendingMovieList: List<TrendingData> = listOf(),
    val trendingTvList: List<TrendingData> = listOf(),
    val trendingPeopleList: List<TrendingData> = listOf(),
    val selectedMediaType: MediaType = MediaType.all,
    val genres: List<Genre> = listOf(),
) : ScreenUiState {
    override fun copyWith(isLoading: Boolean): ScreenUiState = copy(isLoading = isLoading)
}

sealed class TrendingScreenUiEvent {
    data object OnRefresh : TrendingScreenUiEvent()
    data object OnLoadMore : TrendingScreenUiEvent()
    data object ShowTimeWindowDD : TrendingScreenUiEvent()
    data class OnViewAllPressed(val mediaType: MediaType) : TrendingScreenUiEvent()
    data class OnTimeWindowSelected(val index: Int) : TrendingScreenUiEvent()
    data object Dismiss : TrendingScreenUiEvent()
}

@HiltViewModel
class TrendingScreenViewModel @Inject constructor(
    private val getAllTrending: GetAllTrendingUseCase,
    private val getTrendingMovie: GetTrendingMovieUseCase,
    private val getTrendingTv: GetTrendingTvUseCase,
    private val getTrendingPeople: GetTrendingPeopleUseCase,
) : ScreenViewModel<TrendingScreenUiState, TrendingScreenUiEvent>(TrendingScreenUiState()) {

    init {
        refreshAllList()
    }

    private fun refreshAllList() {
        viewModelScope.launch {
            getAllTrendingList()
            getTrendingMovieList()
            getTrendingTvList()
            getTrendingPeopleList()
        }
    }

    private suspend fun getAllTrendingList(pagingEnabled: Boolean = false) {
        getAllTrending(
            pagingEnabled = pagingEnabled,
            timeWindow = timeWindowList[uiState.value.selectedTimeWindowIndex]
        ).collectDataState { dataState ->
            updateUiState { currentState ->
                currentState.copy(
                    allTrendingList = dataState.data,
                )
            }
        }
    }

    private suspend fun getTrendingMovieList(pagingEnabled: Boolean = false) {
        getTrendingMovie(
            pagingEnabled = pagingEnabled,
            timeWindow = timeWindowList[uiState.value.selectedTimeWindowIndex],
        ).collectDataState { dataState ->
            updateUiState { currentState ->
                currentState.copy(
                    trendingMovieList = dataState.data,
                )
            }
        }
    }

    private suspend fun getTrendingTvList(pagingEnabled: Boolean = false) {
        getTrendingTv(
            pagingEnabled = pagingEnabled,
            timeWindow = timeWindowList[uiState.value.selectedTimeWindowIndex],
        ).collectDataState { dataState ->
            updateUiState { currentState ->
                currentState.copy(
                    trendingTvList = dataState.data,
                )
            }
        }
    }

    private suspend fun getTrendingPeopleList(pagingEnabled: Boolean = false) {
        getTrendingPeople(
            pagingEnabled = pagingEnabled,
            timeWindow = timeWindowList[uiState.value.selectedTimeWindowIndex],
        ).collectDataState { dataState ->
            updateUiState { currentState ->
                currentState.copy(
                    trendingPeopleList = dataState.data,
                )
            }
        }
    }

    override fun onUiEvent(event: TrendingScreenUiEvent) {
        when (event) {

            TrendingScreenUiEvent.OnRefresh -> refreshAllList()

            TrendingScreenUiEvent.OnLoadMore -> {
                viewModelScope.launch {
                    when (uiState.value.selectedMediaType) {
                        MediaType.all -> getAllTrendingList(true)
                        MediaType.movie -> getTrendingMovieList(true)
                        MediaType.tv -> getTrendingTvList(true)
                        MediaType.person -> getTrendingPeopleList(true)
                    }
                }
            }

            is TrendingScreenUiEvent.OnViewAllPressed -> {
                updateUiState { currentState ->
                    currentState.copy(
                        selectedMediaType = event.mediaType,
                        screenStack = currentState.screenStack.toMutableList().apply {
                            add(TrendingNavigationItem.VIEW_ALL_LIST)
                        },
                    )
                }
            }

            TrendingScreenUiEvent.ShowTimeWindowDD -> updateUiState { currentState ->
                currentState.copy(screenStack = currentState.screenStack.toMutableList().apply {
                    add(TrendingNavigationItem.TIME_WINDOW_DD)
                })
            }

            is TrendingScreenUiEvent.OnTimeWindowSelected -> {
                updateUiState { currentState ->
                    currentState.copy(
                        screenStack = currentState.screenStack.toMutableList().apply {
                            remove(TrendingNavigationItem.TIME_WINDOW_DD)
                        },
                        selectedTimeWindowIndex = event.index,
                    )
                }
                refreshAllList()
            }

            TrendingScreenUiEvent.Dismiss -> updateUiState { currentState ->
                currentState.copy(screenStack = currentState.screenStack.toMutableList().apply {
                    removeLast()
                })
            }
        }
    }

    override fun showAlert(message: UiText) {
        updateUiState { currentState ->
            currentState.copy(
                screenStack = currentState.screenStack.toMutableList().apply {
                    add(TrendingNavigationItem.ALERT)
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