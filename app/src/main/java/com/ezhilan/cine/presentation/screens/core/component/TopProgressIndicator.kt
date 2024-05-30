package com.ezhilan.cine.presentation.screens.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ezhilan.cine.presentation.config.spacing

@Composable
fun TopProgressIndicator(modifier: Modifier = Modifier, isLoading: Boolean) {
    if (isLoading) {
        LinearProgressIndicator(
            modifier = modifier
                .height(MaterialTheme.spacing.unit1),
            trackColor = MaterialTheme.colorScheme.surface,
        )
    } else {
        Spacer(
            modifier = modifier
                .height(MaterialTheme.spacing.unit1)
                .background(color = MaterialTheme.colorScheme.surface),
        )
    }
}