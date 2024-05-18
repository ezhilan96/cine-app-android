package com.ezhilan.cine.presentation.screens.core.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ezhilan.cine.data.repository.core.ConnectionState

@Composable
fun AnimateConnectionState(
    modifier: Modifier = Modifier,
    connectionState: ConnectionState,
    onConnected: @Composable () -> Unit,
    onDisconnected: @Composable () -> Unit,
    onPending: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {

        AnimatedVisibility(
            visible = connectionState != ConnectionState.Pending,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            onConnected()
            AnimatedVisibility(
                visible = connectionState == ConnectionState.Disconnected,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                onDisconnected()
            }
        }

        AnimatedVisibility(
            visible = connectionState == ConnectionState.Pending,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            onPending()
        }
    }
}