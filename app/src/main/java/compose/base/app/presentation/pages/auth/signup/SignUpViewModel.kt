package compose.base.app.presentation.pages.auth.signup

import androidx.lifecycle.ViewModel
import compose.base.app.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SignupUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val error: String = "",
    val navigateToHome: Boolean = false,
)

sealed class SignupUiEvent {

    data class OnUserNameChange(val name: String) : SignupUiEvent()

    object OnSubmit : SignupUiEvent()
}

@HiltViewModel
class SignupViewModel @Inject constructor(private val signupUseCase: LoginUseCase) : ViewModel() {

    private var _signupUiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState>
        get() = _signupUiState

    fun handleEvent(event: SignupUiEvent) {
        if (!_signupUiState.value.isLoading) {
            when (event) {
                is SignupUiEvent.OnUserNameChange -> _signupUiState.update { currentState ->
                    currentState.copy(userName = event.name)
                }

                SignupUiEvent.OnSubmit -> _signupUiState.update { currentState ->
                    currentState.copy(navigateToHome = true)
                }
            }
        }
    }

    fun onScreenFinish() {
        _signupUiState.update { currentState ->
            currentState.copy(navigateToHome = false)
        }
    }
}