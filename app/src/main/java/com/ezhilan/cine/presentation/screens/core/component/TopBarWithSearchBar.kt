@file:OptIn(ExperimentalMaterial3Api::class)

package com.ezhilan.cine.presentation.screens.core.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import com.ezhilan.cine.presentation.config.spacing

@Composable
fun TopBarWithSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    var isSearchEnabled by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(
            visible = !isSearchEnabled,
            enter = fadeIn() + expandHorizontally(
                expandFrom = Alignment.Start,
                clip = false,
            ),
            exit = fadeOut() + shrinkHorizontally(
                shrinkTowards = Alignment.Start,
                clip = false,
            ),
        ) {
            TopAppBar(
                navigationIcon = title,
                title = {},
                actions = {
                    actions()
                    FilledTonalIconButton(
                        onClick = {
                            isSearchEnabled = true
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                        )
                    }
                },
            )
        }

        AnimatedVisibility(
            modifier = modifier.weight(1f),
            visible = isSearchEnabled,
            enter = fadeIn() + expandHorizontally(
                expandFrom = Alignment.End,
                clip = false,
            ),
            exit = fadeOut() + shrinkHorizontally(
                shrinkTowards = Alignment.End,
                clip = false,
            ),
        ) {
            TextField(
                modifier = modifier
                    .focusRequester(focusRequester)
                    .padding(MaterialTheme.spacing.grid05)
                    .height(MaterialTheme.spacing.height_search_bar)
                    .weight(1f),
                value = query,
                onValueChange = onQueryChanged,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (query.isNotEmpty()) {
                                onQueryChanged("")
                            } else {
                                isSearchEnabled = false
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                },
                placeholder = {
                    Text(text = "Search")
                },
                shape = MaterialTheme.shapes.extraLarge,
                colors = TextFieldDefaults.colors().copy(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
                singleLine = true,
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}