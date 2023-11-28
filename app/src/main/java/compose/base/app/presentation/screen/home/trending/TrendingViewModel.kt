package compose.base.app.presentation.screen.home.trending

import androidx.lifecycle.ViewModel
import compose.base.app.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class TrendingUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val error: String = "",
    val navigateToHome: Boolean = false,
)

sealed class TrendingUiEvent {

    data class OnUserNameChange(val name: String) : TrendingUiEvent()
    object OnSubmit : TrendingUiEvent()
}

@HiltViewModel
class TrendingViewModel @Inject constructor(private val trendingUseCase: LoginUseCase) :
    ViewModel() {

    private var _trendingUiState = MutableStateFlow(TrendingUiState())
    val uiState: StateFlow<TrendingUiState>
        get() = _trendingUiState

    fun handleEvent(event: TrendingUiEvent) {
        if (!_trendingUiState.value.isLoading) {
            when (event) {
                is TrendingUiEvent.OnUserNameChange -> _trendingUiState.update { currentState ->
                    currentState.copy(userName = event.name)
                }

                TrendingUiEvent.OnSubmit -> _trendingUiState.update { currentState ->
                    currentState.copy(navigateToHome = true)
                }
            }
        }
    }

    fun onScreenFinish() {
        _trendingUiState.update { currentState ->
            currentState.copy(navigateToHome = false)
        }
    }
}