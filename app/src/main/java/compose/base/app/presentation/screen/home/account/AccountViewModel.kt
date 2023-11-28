package compose.base.app.presentation.screen.home.account

import androidx.lifecycle.ViewModel
import compose.base.app.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AccountUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val error: String = "",
    val navigateToHome: Boolean = false,
)

sealed class AccountUiEvent {

    data class OnUserNameChange(val name: String) : AccountUiEvent()
    object OnSubmit : AccountUiEvent()
}

@HiltViewModel
class AccountViewModel @Inject constructor(private val accountUseCase: LoginUseCase) : ViewModel() {

    private var _accountUiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState>
        get() = _accountUiState

    fun handleEvent(event: AccountUiEvent) {
        if (!_accountUiState.value.isLoading) {
            when (event) {
                is AccountUiEvent.OnUserNameChange -> _accountUiState.update { currentState ->
                    currentState.copy(userName = event.name)
                }

                AccountUiEvent.OnSubmit -> _accountUiState.update { currentState ->
                    currentState.copy(navigateToHome = true)
                }
            }
        }
    }

    fun onScreenFinish() {
        _accountUiState.update { currentState ->
            currentState.copy(navigateToHome = false)
        }
    }
}