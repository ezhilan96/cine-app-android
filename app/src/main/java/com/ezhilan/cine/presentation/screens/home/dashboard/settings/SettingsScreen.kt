@file:OptIn(ExperimentalMaterial3Api::class)

package com.ezhilan.cine.presentation.screens.home.dashboard.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ezhilan.cine.R
import com.ezhilan.cine.presentation.config.AppThemeList
import com.ezhilan.cine.presentation.config.CineTheme
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.config.textStyle
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

    if (uiState.showRegionDialog) {
        Dialog(onDismissRequest = { viewModel.onUiEvent(SettingsScreenUiEvent.OnDismiss) }) {
            Surface(
                modifier.padding(MaterialTheme.spacing.grid1),
                shape = MaterialTheme.shapes.medium
            ) {
                Column {
                    Text(
                        modifier = modifier.padding(MaterialTheme.spacing.grid1),
                        text = "Select region",
                    )
                    LazyColumn {
                        uiState.regionList.forEachIndexed { index, item ->
                            item {
                                TextButton(
                                    onClick = {
                                        viewModel.onUiEvent(
                                            SettingsScreenUiEvent.OnRegionSelected(index)
                                        )
                                    },
                                    shape = MaterialTheme.shapes.small,
                                ) {
                                    Row(
                                        modifier = modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Text(text = item.first)
                                        if (uiState.selectedRegionIndex == index) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_check),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsScreenUiState,
    uiEvent: (SettingsScreenUiEvent) -> Unit,
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = DashboardRoute.SettingsDestination.label)
            },
        )
    }) { safeAreaPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(safeAreaPadding)
        ) {


            ExposedDropdownMenuBox(modifier = modifier.fillMaxWidth(),
                expanded = uiState.showAppThemeDialog,
                onExpandedChange = {
                    if (it) {
                        uiEvent(SettingsScreenUiEvent.OnThemeButtonPressed)
                    } else {
                        uiEvent(SettingsScreenUiEvent.OnDismiss)
                    }
                }) {
                TextButton(
                    modifier = modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    onClick = { uiEvent(SettingsScreenUiEvent.OnThemeButtonPressed) },
                ) {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = uiState.appTheme.icon),
                                contentDescription = null,
                            )

                            Spacer(modifier = modifier.width(MaterialTheme.spacing.grid1))

                            Text(
                                text = "App theme",
                            )
                        }

                        Text(
                            text = "< ${uiState.appTheme.name} >",
                            style = MaterialTheme.textStyle.settingsLabelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                DropdownMenu(
                    expanded = uiState.showAppThemeDialog,
                    onDismissRequest = { uiEvent(SettingsScreenUiEvent.OnDismiss) },
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

            HorizontalDivider(modifier = modifier.padding(horizontal = MaterialTheme.spacing.grid1))

            TextButton(onClick = { uiEvent(SettingsScreenUiEvent.OnRegionButtonPressed) }) {

                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_region),
                            contentDescription = null,
                        )

                        Spacer(modifier = modifier.width(MaterialTheme.spacing.grid1))

                        Text(
                            text = "Region",
                        )
                    }

                    Text(
                        text = "< ${uiState.selectedRegionIndex?.let { uiState.regionList[it].first } ?: "None"} >",
                        style = MaterialTheme.textStyle.settingsLabelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            HorizontalDivider(modifier = modifier.padding(horizontal = MaterialTheme.spacing.grid1))

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