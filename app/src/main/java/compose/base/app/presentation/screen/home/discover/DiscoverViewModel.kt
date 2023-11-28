package compose.base.app.presentation.screen.home.discover

import androidx.lifecycle.ViewModel
import compose.base.app.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DiscoverUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val error: String = "",
    val navigateToHome: Boolean = false,
)

sealed class DiscoverUiEvent {

    data class OnUserNameChange(val name: String) : DiscoverUiEvent()
    object OnSubmit : DiscoverUiEvent()
}

@HiltViewModel
class DiscoverViewModel @Inject constructor(private val discoverUseCase: LoginUseCase) :
    ViewModel() {

    private var _discoverUiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState>
        get() = _discoverUiState

    fun handleEvent(event: DiscoverUiEvent) {
        if (!_discoverUiState.value.isLoading) {
            when (event) {
                is DiscoverUiEvent.OnUserNameChange -> _discoverUiState.update { currentState ->
                    currentState.copy(userName = event.name)
                }

                DiscoverUiEvent.OnSubmit -> _discoverUiState.update { currentState ->
                    currentState.copy(navigateToHome = true)
                }
            }
        }
    }

    fun onScreenFinish() {
        _discoverUiState.update { currentState ->
            currentState.copy(navigateToHome = false)
        }
    }
}