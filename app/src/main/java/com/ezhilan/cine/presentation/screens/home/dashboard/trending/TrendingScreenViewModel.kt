package com.ezhilan.cine.presentation.screens.home.dashboard.trending

import com.ezhilan.cine.core.Constants
import com.ezhilan.cine.presentation.screens.core.ScreenUiState
import com.ezhilan.cine.presentation.screens.core.ScreenViewModel
import com.ezhilan.cine.presentation.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class TrendingNavigationItem {
    ALERT,
    TIME_WINDOW_DD,
    LANGUAGE_DD,
}

data class TrendingScreenUiState(
    override val isLoading: Boolean = false,
    override val screenStack: List<TrendingNavigationItem> = listOf(),
    override val alertMessage: UiText = UiText.Value(),
    val timeWindowList: List<String> = listOf("Today", "This Week"),
    val languageList: List<String> = Constants.lanuages,
    val selectedTimeWindowIndex: Int = 0,
    val selectedLanguageIndex: Int = 0,
) : ScreenUiState {
    override fun copyWith(isLoading: Boolean): ScreenUiState = copy(isLoading = isLoading)
}

sealed class TrendingScreenUiEvent {
    data object ShowTimeWindowDD : TrendingScreenUiEvent()
    data class OnTimeWindowSelected(val index: Int) : TrendingScreenUiEvent()
    data object ShowLanguageDD : TrendingScreenUiEvent()
    data class OnLanguageSelected(val index: Int) : TrendingScreenUiEvent()
    data object Dismiss : TrendingScreenUiEvent()
}

@HiltViewModel
class TrendingScreenViewModel @Inject constructor() :
    ScreenViewModel<TrendingScreenUiState, TrendingScreenUiEvent>(TrendingScreenUiState()) {

    override fun onUiEvent(event: TrendingScreenUiEvent) {
        when (event) {
            TrendingScreenUiEvent.ShowTimeWindowDD -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(TrendingNavigationItem.TIME_WINDOW_DD)
                    }
                )
            }

            is TrendingScreenUiEvent.OnTimeWindowSelected -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        remove(TrendingNavigationItem.TIME_WINDOW_DD)
                    },
                    selectedTimeWindowIndex = event.index,
                )
            }

            TrendingScreenUiEvent.ShowLanguageDD -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        add(TrendingNavigationItem.LANGUAGE_DD)
                    }
                )
            }

            is TrendingScreenUiEvent.OnLanguageSelected -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        remove(TrendingNavigationItem.LANGUAGE_DD)
                    },
                    selectedLanguageIndex = event.index,
                )
            }

            TrendingScreenUiEvent.Dismiss -> updateUiState { currentState ->
                currentState.copy(
                    screenStack = currentState.screenStack.toMutableList().apply {
                        removeLast()
                    }
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