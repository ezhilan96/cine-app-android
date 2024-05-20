package com.ezhilan.cine.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezhilan.cine.data.repository.core.ConnectionState
import com.ezhilan.cine.domain.repository.AuthRepository
import com.ezhilan.cine.domain.repository.core.NetworkConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SessionState {
    INIT,
    EXPIRED,
    ACTIVE,
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkRepo: NetworkConnectionRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    val connectionState: Flow<ConnectionState> = networkRepo.connectionState

    private val _sessionState: MutableStateFlow<SessionState> =
        MutableStateFlow(SessionState.INIT)
    val sessionState: StateFlow<SessionState> = _sessionState

    init {
        viewModelScope.launch {
            networkRepo.connectionState.filter { it == ConnectionState.Connected }.first().let {
                checkLoginState()
            }
        }
    }

    private fun checkLoginState() = viewModelScope.launch {
        authRepo.isLoggedIn.collect {
            _sessionState.value = if (it) SessionState.ACTIVE else SessionState.EXPIRED
        }
    }

    fun checkConnection() = networkRepo.checkConnection()
}