package com.ezhilan.cine.presentation.screens.home.dashboard.trending

import androidx.lifecycle.viewModelScope
import com.ezhilan.cine.data.model.remote.response.home.Genre
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.domain.useCases.home.trendingList.GetTrendingListUseCase
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
    val allTrendingList: MutableList<MediaData> = mutableListOf(),
    val trendingMovieList: MutableList<MediaData> = mutableListOf(),
    val trendingTvList: MutableList<MediaData> = mutableListOf(),
    val trendingPeopleList: MutableList<MediaData> = mutableListOf(),
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
    private val getTrendingList: GetTrendingListUseCase,
) : ScreenViewModel<TrendingScreenUiState, TrendingScreenUiEvent>(TrendingScreenUiState()) {

    init {
        refreshAllList()
    }

    private fun refreshAllList() {
        viewModelScope.launch {
            getTrendingList(mediaType = MediaType.all)
            getTrendingList(mediaType = MediaType.movie)
            getTrendingList(mediaType = MediaType.tv)
            getTrendingList(mediaType = MediaType.person)
        }
    }

    private suspend fun getTrendingList(
        pagingEnabled: Boolean = false,
        mediaType: MediaType,
    ) {
        getTrendingList(
            pagingEnabled = pagingEnabled,
            timeWindow = timeWindowList[uiState.value.selectedTimeWindowIndex],
            mediaType = mediaType,
        ).collectDataState { dataState ->
            updateUiState { currentState ->
                when (mediaType) {
                    MediaType.all -> currentState.copy(allTrendingList = mutableListOf<MediaData>().apply {
                        addAll(dataState.data)
                    })

                    MediaType.movie -> currentState.copy(trendingMovieList = mutableListOf<MediaData>().apply {
                        addAll(dataState.data)
                    })

                    MediaType.tv -> currentState.copy(trendingTvList = mutableListOf<MediaData>().apply {
                        addAll(dataState.data)
                    })

                    MediaType.person -> currentState.copy(trendingPeopleList = mutableListOf<MediaData>().apply {
                        addAll(dataState.data)
                    })
                }
            }
        }
    }

    override fun onUiEvent(event: TrendingScreenUiEvent) {
        when (event) {

            TrendingScreenUiEvent.OnRefresh -> refreshAllList()

            TrendingScreenUiEvent.OnLoadMore -> viewModelScope.launch {
                getTrendingList(
                    pagingEnabled = true,
                    mediaType = uiState.value.selectedMediaType,
                )
            }

            is TrendingScreenUiEvent.OnViewAllPressed -> updateUiState { currentState ->
                currentState.copy(
                    selectedMediaType = event.mediaType,
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(TrendingNavigationItem.VIEW_ALL_LIST)
                    },
                )
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
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        removeLast()
                    },
                )
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