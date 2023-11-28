package compose.base.app.presentation.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import compose.base.app.config.util.NetworkResponse
import compose.base.app.data.model.response.Error
import compose.base.app.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val error: String = "",
    val navigateToHome: Boolean = false,
    val navigateToSignup: Boolean = false,
    val navigateToForgot: Boolean = false,
)

sealed class LoginUiEvent {

    data class OnUserNameChange(val name: String) : LoginUiEvent()

    object OnForgotPasswordClicked : LoginUiEvent()

    object OnSignUpClicked : LoginUiEvent()
    object OnSubmit : LoginUiEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private var _loginUiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState>
        get() = _loginUiState

    fun handleEvent(event: LoginUiEvent) {
        if (!_loginUiState.value.isLoading) {
            when (event) {
                is LoginUiEvent.OnUserNameChange -> _loginUiState.update { currentState ->
                    currentState.copy(userName = event.name)
                }

                LoginUiEvent.OnForgotPasswordClicked -> _loginUiState.update { currentState ->
                    currentState.copy(navigateToForgot = true)
                }

                LoginUiEvent.OnSignUpClicked -> _loginUiState.update { currentState ->
                    currentState.copy(navigateToSignup = true)
                }

                LoginUiEvent.OnSubmit -> submit()
            }
        }
    }

    private fun submit() {
        viewModelScope.launch {
            loginUseCase(_loginUiState.value.userName).collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _loginUiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                navigateToHome = true,
                            )
                        }
                    }

                    is NetworkResponse.Error -> {
                        _loginUiState.update { currentState ->
                            currentState.copy(
                                error = response.message
                            )
                        }
                    }

                    is NetworkResponse.Exception -> {
                        _loginUiState.update { currentState ->
                            currentState.copy(
                                error = response.e.message ?: Error().message
                            )
                        }
                    }
                }
            }
        }
    }

    fun onScreenFinish() {
        _loginUiState.update { currentState ->
            currentState.copy(
                navigateToHome = false,
                navigateToSignup = false,
                navigateToForgot = false,
            )
        }
    }
}