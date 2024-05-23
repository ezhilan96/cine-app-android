package com.ezhilan.cine.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezhilan.cine.data.repository.core.ConnectionState
import com.ezhilan.cine.domain.repository.AuthRepository
import com.ezhilan.cine.domain.repository.core.NetworkConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SessionState {
    INIT,
    EXPIRED,
    ACTIVE,
}

data class MainScreenUiState(
    val connectionState: ConnectionState = ConnectionState.Pending,
    val sessionState: SessionState = SessionState.INIT,
    var requestToken: String? = null,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkRepo: NetworkConnectionRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState

    init {
        viewModelScope.launch {
            networkRepo.connectionState.collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        connectionState = it
                    )
                }
            }
        }
        viewModelScope.launch {
            authRepo.isSessionActive.collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        sessionState = if (it) SessionState.ACTIVE else SessionState.EXPIRED
                    )
                }
            }
        }
    }

    fun checkConnection() = networkRepo.checkConnection()
}