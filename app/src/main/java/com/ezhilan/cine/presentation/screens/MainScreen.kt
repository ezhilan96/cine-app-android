package com.ezhilan.cine.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ezhilan.cine.data.repository.core.ConnectionState
import com.ezhilan.cine.presentation.screens.core.component.AnimateConnectionState
import com.ezhilan.cine.presentation.screens.core.screen.NoInternetScreen
import com.ezhilan.cine.presentation.screens.core.screen.SplashScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val connectionState by viewModel.connectionState.collectAsStateWithLifecycle(ConnectionState.Pending)
    val sessionState by viewModel.sessionState.collectAsStateWithLifecycle()
    AnimateConnectionState(
        modifier = modifier.fillMaxSize(),
        connectionState = connectionState,
        onConnected = {
            MainNavHost(
                modifier = modifier,
                sessionState = sessionState,
            )
        },
        onDisconnected = { NoInternetScreen(onRetryConnection = viewModel::checkConnection) },
        onPending = { SplashScreen(modifier = modifier) },
    )
}