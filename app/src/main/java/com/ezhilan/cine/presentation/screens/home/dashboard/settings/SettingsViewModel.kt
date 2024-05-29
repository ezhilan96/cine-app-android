package com.ezhilan.cine.presentation.screens.home.dashboard.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezhilan.cine.core.Constants
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
    val showAppThemeDialog: Boolean = false,
    val showRegionDialog: Boolean = false,
    val regionList: List<Pair<String, String>> = Constants.regions,
    val selectedRegionIndex: Int? = null,
)

sealed class SettingsScreenUiEvent {
    data class OnThemeChanged(val appTheme: AppTheme) : SettingsScreenUiEvent()
    data object OnThemeButtonPressed : SettingsScreenUiEvent()
    data object OnRegionButtonPressed : SettingsScreenUiEvent()
    data class OnRegionSelected(val index: Int) : SettingsScreenUiEvent()
    data object OnDismiss : SettingsScreenUiEvent()
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
        viewModelScope.launch {
            dataStore.region.collect { region ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        selectedRegionIndex = currentUiState.regionList.firstOrNull {
                            it.second == region
                        }?.let {
                            currentUiState.regionList.indexOf(it)
                        }
                    )
                }
            }
        }
    }

    private val _uiState = MutableStateFlow(SettingsScreenUiState())
    val uiState: StateFlow<SettingsScreenUiState> = _uiState

    fun onUiEvent(event: SettingsScreenUiEvent) {
        when (event) {
            is SettingsScreenUiEvent.OnThemeChanged -> {
                viewModelScope.launch { dataStore.setAppTheme(event.appTheme) }
                _uiState.update { currentUiState ->
                    currentUiState.copy(showAppThemeDialog = false)
                }
            }

            SettingsScreenUiEvent.OnThemeButtonPressed -> _uiState.update { currentUiState ->
                currentUiState.copy(showAppThemeDialog = true)
            }

            SettingsScreenUiEvent.OnRegionButtonPressed -> _uiState.update { currentUiState ->
                currentUiState.copy(showRegionDialog = true)
            }

            is SettingsScreenUiEvent.OnRegionSelected -> {
                viewModelScope.launch {
                    dataStore.setRegion(uiState.value.regionList[event.index].second)
                }
                _uiState.update { currentUiState ->
                    currentUiState.copy(showRegionDialog = false)
                }
            }

            SettingsScreenUiEvent.OnDismiss -> _uiState.update { currentUiState ->
                currentUiState.copy(
                    showAppThemeDialog = false,
                    showRegionDialog = false,
                )
            }
        }
    }
}