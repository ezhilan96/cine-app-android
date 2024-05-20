package com.ezhilan.cine.presentation.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ezhilan.cine.presentation.screens.core.component.AnimateConnectionState
import com.ezhilan.cine.presentation.screens.core.screen.NoInternetScreen
import com.ezhilan.cine.presentation.screens.core.screen.SplashScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AnimateConnectionState(
        modifier = modifier.fillMaxSize(),
        connectionState = uiState.connectionState,
        onConnected = {
            MainNavHost(
                modifier = modifier,
                sessionState = uiState.sessionState,
            )
        },
        onDisconnected = { NoInternetScreen(onRetryConnection = viewModel::checkConnection) },
        onPending = { SplashScreen(modifier = modifier) },
    )
    LaunchedEffect(uiState.sessionState) {
        if (uiState.sessionState == SessionState.EXPIRED) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
            ContextCompat.startActivity(context, browserIntent, null)
        }
    }
}