@file:OptIn(ExperimentalMaterial3Api::class)

package com.ezhilan.cine.presentation.screens.home.dashboard.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ezhilan.cine.R
import com.ezhilan.cine.presentation.config.AppThemeList
import com.ezhilan.cine.presentation.config.CineTheme
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.screens.routes.DashboardRoute

@Composable
fun SettingsDestination(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        modifier = modifier,
        uiState = uiState,
        uiEvent = viewModel::onUiEvent,
    )
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsScreenUiState,
    uiEvent: (SettingsScreenUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Text(text = DashboardRoute.SettingsDestination.label)
                },
                actions = {
                    ExposedDropdownMenuBox(
                        expanded = uiState.appThemeDialogVisible,
                        onExpandedChange = {
                            if (it) {
                                uiEvent(SettingsScreenUiEvent.OnThemeButtonPressed)
                            } else {
                                uiEvent(SettingsScreenUiEvent.OnThemeDropDownDialogDismissed)
                            }
                        }
                    ) {
                        FilledTonalIconButton(
                            modifier = modifier.menuAnchor(),
                            onClick = { uiEvent(SettingsScreenUiEvent.OnThemeButtonPressed) },
                        ) {
                            Icon(
                                painter = painterResource(id = uiState.appTheme.icon),
                                contentDescription = null,
                            )
                        }
                        DropdownMenu(
                            expanded = uiState.appThemeDialogVisible,
                            onDismissRequest = { uiEvent(SettingsScreenUiEvent.OnThemeDropDownDialogDismissed) },
                        ) {
                            AppThemeList.forEach { theme ->
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(theme.icon),
                                            contentDescription = null,
                                        )
                                    },
                                    text = { Text(theme.name) },
                                    trailingIcon = {
                                        if (uiState.appTheme == theme) {
                                            Icon(
                                                painter = painterResource(R.drawable.ic_check),
                                                contentDescription = null,
                                            )
                                        }
                                    },
                                    onClick = { uiEvent(SettingsScreenUiEvent.OnThemeChanged(theme)) },
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { safeAreaPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(safeAreaPadding)
        ) {

        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    CineTheme {
        SettingsScreen(
            modifier = Modifier,
            uiState = SettingsScreenUiState(),
            uiEvent = {},
        )
    }
}