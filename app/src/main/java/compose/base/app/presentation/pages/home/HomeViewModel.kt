package compose.base.app.presentation.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import compose.base.app.MainRoutes
import compose.base.app.config.util.NetworkResponse
import compose.base.app.data.model.response.Error
import compose.base.app.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    var isLoading: Boolean = false,
    var userName: String = "",
    var error: String = "",
)

sealed class HomeUiEvent {

    data class OnUserNameChange(val name: String) : HomeUiEvent()

    object OnSubmit : HomeUiEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeUseCase: LoginUseCase) : ViewModel() {

    private var _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState>
        get() = _homeUiState

    lateinit var navController: NavController

    fun handleEvent(event: HomeUiEvent) {
        if (!_homeUiState.value.isLoading) {
            when (event) {
                is HomeUiEvent.OnUserNameChange -> {
                    _homeUiState.update { currentState -> currentState.copy(userName = event.name) }
                }

                HomeUiEvent.OnSubmit -> {
                    _homeUiState.update { currentState -> currentState.copy(isLoading = true) }
                    formSubmit()
                }
            }
        }
    }

    private fun formSubmit() {
        viewModelScope.launch {
            homeUseCase(_homeUiState.value.userName).collect { response ->
                when (response) {
                    is NetworkResponse.Success -> navController.navigate(
                        MainRoutes.HomeScreen.route + "/" + response.responseData.token
                    )

                    is NetworkResponse.Error -> _homeUiState.update { currentState ->
                        currentState.copy(
                            error = response.message
                        )
                    }

                    is NetworkResponse.Exception -> _homeUiState.update { currentState ->
                        currentState.copy(
                            error = response.e.message ?: Error().message
                        )
                    }
                }
            }
        }
    }
}