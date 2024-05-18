@file:OptIn(ExperimentalMaterial3Api::class)

package com.ezhilan.cine.presentation.screens.core.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.ezhilan.cine.presentation.config.CineTheme

@Composable
fun PullToRefreshContainer(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val localModifier = Modifier
    val pullToRefreshState = rememberPullToRefreshState()
    Box(modifier = modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
        content()
        androidx.compose.material3.pulltorefresh.PullToRefreshContainer(
            modifier = localModifier
                .align(Alignment.TopCenter),
            state = pullToRefreshState,
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.scrim,
        )
    }

    LaunchedEffect(pullToRefreshState.isRefreshing) {
        if (pullToRefreshState.isRefreshing) {
            onRefresh()
        }
    }

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            pullToRefreshState.endRefresh()
        }
    }
}

@Preview
@Composable
private fun PullToRefreshContainerPreview() {
    CineTheme {
        Scaffold { paddingValues ->
            PullToRefreshContainer(
                Modifier.padding(paddingValues),
                isLoading = true,
                onRefresh = { },
            ) {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(100) {
                            Text(text = "$it")
                        }
                    }
                }
            }
        }
    }
}