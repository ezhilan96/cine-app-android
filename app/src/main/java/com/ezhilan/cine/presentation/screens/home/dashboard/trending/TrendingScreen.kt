@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.ezhilan.cine.presentation.screens.home.dashboard.trending

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.ezhilan.cine.R
import com.ezhilan.cine.domain.entity.AllTrendingData
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.presentation.config.CineTheme
import com.ezhilan.cine.presentation.config.colors
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.config.textStyle
import java.util.Locale

@Composable
fun TrendingDestination(
    modifier: Modifier = Modifier,
    viewModel: TrendingScreenViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    TrendingScreen(
        modifier = modifier,
        uiState = uiState.value,
        uiEvent = viewModel::onUiEvent,
    )
}

@Composable
fun TrendingScreen(
    modifier: Modifier = Modifier,
    uiState: TrendingScreenUiState,
    uiEvent: (TrendingScreenUiEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            modifier = modifier.height(IntrinsicSize.Max),
                            text = "Trending",
                        )
                        Spacer(modifier = modifier.width(MaterialTheme.spacing.unit4))
                        ExposedDropdownMenuBox(
                            modifier = modifier.padding(bottom = MaterialTheme.spacing.grid05),
                            expanded = uiState.screenStack.contains(TrendingNavigationItem.TIME_WINDOW_DD),
                            onExpandedChange = {
                                if (!it) {
                                    uiEvent(TrendingScreenUiEvent.Dismiss)
                                }
                            },
                        ) {
                            TextButton(
                                modifier = modifier
                                    .menuAnchor()
                                    .height(MaterialTheme.spacing.grid2),
                                onClick = { uiEvent(TrendingScreenUiEvent.ShowTimeWindowDD) },
                                contentPadding = PaddingValues(0.dp),
                                shape = MaterialTheme.shapes.extraSmall,
                            ) {
                                Text("[${uiState.timeWindowList[uiState.selectedTimeWindowIndex]}]")
                            }
                            DropdownMenu(
                                expanded = uiState.screenStack.contains(TrendingNavigationItem.TIME_WINDOW_DD),
                                onDismissRequest = { uiEvent(TrendingScreenUiEvent.Dismiss) },
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Row {
                                            Text(text = "Today")
                                            if (uiState.selectedTimeWindowIndex == 0) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_check),
                                                    contentDescription = null,
                                                )
                                            }
                                        }
                                    },
                                    onClick = { uiEvent(TrendingScreenUiEvent.OnTimeWindowSelected(0)) },
                                )
                                DropdownMenuItem(
                                    text = {
                                        Row {
                                            Text(text = "This week")
                                            if (uiState.selectedTimeWindowIndex == 1) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_check),
                                                    contentDescription = null,
                                                )
                                            }
                                        }
                                    },
                                    onClick = { uiEvent(TrendingScreenUiEvent.OnTimeWindowSelected(1)) },
                                )
                            }
                        }
                    }
                },
                actions = {
                    ExposedDropdownMenuBox(
                        expanded = uiState.screenStack.contains(TrendingNavigationItem.LANGUAGE_DD),
                        onExpandedChange = {
                            if (!it) {
                                uiEvent(TrendingScreenUiEvent.Dismiss)
                            }
                        },
                    ) {
                        IconButton(modifier = modifier.menuAnchor(),
                            onClick = { uiEvent(TrendingScreenUiEvent.ShowLanguageDD) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_globe),
                                contentDescription = null,
                            )
                        }
                        DropdownMenu(
                            expanded = uiState.screenStack.contains(TrendingNavigationItem.LANGUAGE_DD),
                            onDismissRequest = { uiEvent(TrendingScreenUiEvent.Dismiss) },
                        ) {
                            uiState.languageList.forEachIndexed { index, language ->
                                DropdownMenuItem(
                                    text = {
                                        Row {
                                            Text(text = language)
                                            if (uiState.selectedLanguageIndex == index) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_check),
                                                    contentDescription = null,
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        uiEvent(TrendingScreenUiEvent.OnLanguageSelected(index))
                                    },
                                )

                            }
                        }
                    }
                },
            )
        },
    ) { safeAreaPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(safeAreaPadding),
        ) {
            AllTrendingSection(trendingList = uiState.trendingList)
        }
    }
}

@Composable
fun AllTrendingSection(
    modifier: Modifier = Modifier,
    trendingList: List<AllTrendingData>,
) {
    val localDensity = LocalDensity.current
    var screenWidth by remember { mutableStateOf(0.dp) }
    val pagerState = rememberPagerState(pageCount = { trendingList.size })

    Row(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                screenWidth = with(localDensity) { coordinates.size.width.toDp() }
            }
            .aspectRatio(16f / 9f)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalPager(
            modifier = modifier
                .width(screenWidth)
                .fillMaxHeight(),
            state = pagerState,
            pageSize = PageSize.Fill,
        ) { page ->
            Surface(
                modifier = modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Box(
                    modifier = modifier.fillMaxSize()
                ) {
                    BackdropImage(imagePath = trendingList[page].backdropPath ?: "")

                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.surface,
                                    ),
                                ),
                            )
                    )

                    Row(
                        modifier = modifier
                            .align(Alignment.BottomStart)
                            .fillMaxHeight(.5f)
                            .padding(MaterialTheme.spacing.grid1)
                    ) {
                        PosterImage(imagePath = trendingList[page].posterPath ?: "")

                        Spacer(modifier = modifier.width(MaterialTheme.spacing.grid1))

                        Column(modifier = modifier.weight(1f)) {
                            Row {
                                Text(
                                    modifier = modifier
                                        .alignByBaseline()
                                        .weight(1f),
                                    text = trendingList[page].title,
                                    style = MaterialTheme.textStyle.trendingCardTitle,
                                )
                                Spacer(modifier = modifier.width(MaterialTheme.spacing.grid1))
                                Row(
                                    modifier = modifier.alignByBaseline(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Icon(
                                        modifier = modifier.size(MaterialTheme.spacing.size_rating_star),
                                        painter = painterResource(id = R.drawable.ic_rating),
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.star_yellow,
                                    )
                                    Spacer(modifier = modifier.width(MaterialTheme.spacing.grid05))
                                    Text(
                                        text = trendingList[page].rating,
                                        style = MaterialTheme.textStyle.trendingCardRating,
                                        maxLines = 1,
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    modifier = modifier
                                        .width(IntrinsicSize.Min)
                                        .background(
                                            color = MaterialTheme.colorScheme.inverseSurface,
                                            shape = MaterialTheme.shapes.extraSmall,
                                        )
                                        .padding(horizontal = MaterialTheme.spacing.unit4)
                                        .padding(vertical = MaterialTheme.spacing.unit2),
                                    text = trendingList[page].mediaType.toString()
                                        .replaceFirstChar { it.titlecase(Locale.ROOT) },
                                    style = MaterialTheme.textStyle.trendingCardMediaType,
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                )
                                Text(text = " • ")
                                Text(
                                    text = trendingList[page].releaseYear,
                                    style = MaterialTheme.textStyle.trendingCardYear,
                                )
                                Text(text = " • ")
                                Text(
                                    text = trendingList[page].genres.mapNotNull { it.name }
                                        .joinToString(", "),
                                    style = MaterialTheme.textStyle.trendingCardGenre, maxLines = 1,
                                )
                            }
                            Text(
                                modifier = modifier.weight(1f),
                                text = trendingList[page].overview,
                                style = MaterialTheme.textStyle.trendingCardOverview,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }

        TextButton(onClick = { /*TODO*/ }) {
            Text(
                text = "View all",
                style = LocalTextStyle.current.copy(textDecoration = TextDecoration.Underline),
            )
            Spacer(modifier = modifier.width(MaterialTheme.spacing.grid05))
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun BackdropImage(modifier: Modifier = Modifier, imagePath: String) {
    val painter = rememberAsyncImagePainter(
        stringResource(R.string.image_base_url, imagePath)
    )
    Box(modifier) {
        Image(
            modifier = modifier.fillMaxSize(),
            painter = painter,
            contentDescription = null,
        )
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    RotatingHourGlass(
                        modifier = modifier
                            .align(Alignment.Center)
                            .size(MaterialTheme.spacing.unit50)
                    )
                }

                AsyncImagePainter.State.Empty, is AsyncImagePainter.State.Error -> {
                    Image(
                        modifier = modifier
                            .align(Alignment.Center)
                            .size(MaterialTheme.spacing.unit50),
                        painter = painterResource(id = R.drawable.ic_corrupt_image),
                        contentDescription = null,
                    )
                }

                else -> {}
            }
        }
    }
}

@Composable
fun PosterImage(modifier: Modifier = Modifier, imagePath: String) {
    val painter = rememberAsyncImagePainter(
        stringResource(R.string.image_base_url, imagePath)
    )
    Box(
        modifier
            .fillMaxHeight()
            .aspectRatio(3f / 4f)
    ) {
        Image(
            modifier = modifier.fillMaxSize(),
            painter = painter,
            contentDescription = null,
        )
        if (painter.state !is AsyncImagePainter.State.Success) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                if (painter.state is AsyncImagePainter.State.Loading) {
                    RotatingHourGlass(
                        modifier = modifier
                            .align(Alignment.Center)
                            .size(MaterialTheme.spacing.grid3)
                    )
                } else {
                    Image(
                        modifier = modifier
                            .align(Alignment.Center)
                            .size(MaterialTheme.spacing.grid3),
                        painter = painterResource(id = R.drawable.ic_corrupt_image),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun RotatingHourGlass(modifier: Modifier = Modifier) {
    var currentRotation by remember { mutableFloatStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }
    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = currentRotation + 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        ) {
            currentRotation = value
        }
    }
    Image(
        modifier = modifier.rotate(rotation.value),
        painter = painterResource(id = R.drawable.ic_hourglass),
        contentDescription = null,
    )
}

@Preview
@Composable
private fun TrendingScreenPreview() {
    CineTheme {
        TrendingScreen(
            uiState = TrendingScreenUiState(),
            uiEvent = {},
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun AllTrendingSectionPreview() {
    CineTheme {
        Scaffold {
            AllTrendingSection(
                modifier = Modifier.fillMaxWidth(),
                trendingList = listOf(
                    AllTrendingData(
                        id = "",
                        title = "Title",
                        overview = "Overview",
                        backdropPath = "/u1CqlLecfpcuOaugKi3ol9gDQHJ.jpg",
                        posterPath = "/vlHJfLsduZiILN8eYdN57kHZTcQ.jpg",
                        releaseYear = "2022",
                        mediaType = MediaType.tv,
                        genres = listOf(),
                        rating = "7.8",
                    )
                ),
            )
        }
    }
}