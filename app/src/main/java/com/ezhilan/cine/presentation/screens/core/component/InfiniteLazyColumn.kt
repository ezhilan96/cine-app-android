package com.ezhilan.cine.presentation.screens.core.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ezhilan.cine.presentation.util.reachedBottom

@Composable
fun InfiniteLazyColumn(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    onLoadMore: () -> Unit,
    content: LazyListScope.() -> Unit,
) {
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            lazyListState.reachedBottom()
        }
    }
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        content = content,
    )
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            onLoadMore()
        }
    }
}

@Composable
fun InfiniteLazyVerticalGrid(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    onLoadMore: () -> Unit,
    columns: GridCells,
    content: LazyGridScope.() -> Unit,
) {
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            lazyGridState.reachedBottom()
        }
    }
    LazyVerticalGrid(
        modifier = modifier,
        state = lazyGridState,
        columns = columns,
        content = content,
    )
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            onLoadMore()
        }
    }
}