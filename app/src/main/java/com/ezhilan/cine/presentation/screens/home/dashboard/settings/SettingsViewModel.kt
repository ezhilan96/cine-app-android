package com.ezhilan.cine.presentation.screens.home.dashboard.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezhilan.cine.core.TAG
import com.ezhilan.cine.data.dataSource.local.dataStore.UserPreferencesDataStore
import com.ezhilan.cine.presentation.config.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsScreenUiState(
    val appTheme: AppTheme = AppTheme.Light,
    val appThemeDialogVisible: Boolean = false,
)

sealed class SettingsScreenUiEvent {
    data class OnThemeChanged(val appTheme: AppTheme) : SettingsScreenUiEvent()
    data object OnThemeButtonPressed : SettingsScreenUiEvent()
    data object OnThemeDropDownDialogDismissed : SettingsScreenUiEvent()
}


@HiltViewModel
class SettingsViewModel @Inject constructor(private val dataStore: UserPreferencesDataStore) :
    ViewModel() {

    init {
        viewModelScope.launch {
            dataStore.appTheme.collect { appTheme ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(appTheme = appTheme)
                }
            }
        }
    }

    private val _uiState = MutableStateFlow(SettingsScreenUiState())
    val uiState: StateFlow<SettingsScreenUiState> = _uiState

    fun onUiEvent(event: SettingsScreenUiEvent) {
        when (event) {
            is SettingsScreenUiEvent.OnThemeChanged -> {
                Log.i(TAG, "onUiEvent: ${event.appTheme}")
                viewModelScope.launch { dataStore.setAppTheme(event.appTheme) }
                _uiState.update { currentUiState ->
                    currentUiState.copy(appThemeDialogVisible = false)
                }
            }

            SettingsScreenUiEvent.OnThemeButtonPressed -> _uiState.update { currentUiState ->
                currentUiState.copy(appThemeDialogVisible = true)
            }

            SettingsScreenUiEvent.OnThemeDropDownDialogDismissed ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(appThemeDialogVisible = false)
                }
        }
    }
}