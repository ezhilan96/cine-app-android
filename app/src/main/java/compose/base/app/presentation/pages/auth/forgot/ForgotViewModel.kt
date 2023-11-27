package compose.base.app.presentation.pages.auth.forgot

import androidx.lifecycle.ViewModel
import compose.base.app.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ForgotUiState(
    val isLoading: Boolean = false,
    val password: String = "",
    val confirmPassword: String = "",
    val error: String = "",
    val navigateToHome: Boolean = false,
)

sealed class ForgotUiEvent {

    data class OnPasswordChanged(val password: String) : ForgotUiEvent()

    data class OnConfirmPasswordChanged(val password: String) : ForgotUiEvent()

    object OnSubmit : ForgotUiEvent()
}

@HiltViewModel
class ForgotViewModel @Inject constructor(private val forgotUseCase: LoginUseCase) : ViewModel() {

    private var _forgotUiState = MutableStateFlow(ForgotUiState())
    val uiState: StateFlow<ForgotUiState>
        get() = _forgotUiState

    fun handleEvent(event: ForgotUiEvent) {
        if (!_forgotUiState.value.isLoading) {
            when (event) {
                is ForgotUiEvent.OnPasswordChanged -> _forgotUiState.update { currentState ->
                    currentState.copy(password = event.password)
                }

                is ForgotUiEvent.OnConfirmPasswordChanged -> _forgotUiState.update { currentState ->
                    currentState.copy(confirmPassword = event.password)
                }

                ForgotUiEvent.OnSubmit -> _forgotUiState.update { currentState ->
                    currentState.copy(navigateToHome = true)
                }
            }
        }
    }

    fun onScreenFinish() {
        _forgotUiState.update { currentState ->
            currentState.copy(navigateToHome = false)
        }
    }
}