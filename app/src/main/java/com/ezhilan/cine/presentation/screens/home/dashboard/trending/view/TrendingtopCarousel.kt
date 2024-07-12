@file:OptIn(ExperimentalFoundationApi::class)

package com.ezhilan.cine.presentation.screens.home.dashboard.trending.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezhilan.cine.R
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.presentation.config.CineTheme
import com.ezhilan.cine.presentation.config.colors
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.config.textStyle
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.components.BackdropImage
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.components.PosterImage
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun TrendingtopCarousel(
    modifier: Modifier = Modifier,
    trendingList: List<MediaData>,
    onViewAllClick: () -> Unit,
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
                                    style = MaterialTheme.textStyle.carouselTitle,
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
                                        text = trendingList[page].rating ?: "-",
                                        style = MaterialTheme.textStyle.carouselRating,
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
                                    style = MaterialTheme.textStyle.carouselMediaType,
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                )
                                Text(text = " • ")
                                Text(
                                    text = trendingList[page].releaseYear ?: "-",
                                    style = MaterialTheme.textStyle.carouselYear,
                                )
                                Text(text = " • ")
                                Text(
                                    text = trendingList[page].genres?.mapNotNull { it.name }
                                        ?.joinToString(", ") ?: "-",
                                    style = MaterialTheme.textStyle.carouselGenre,
                                    maxLines = 1,
                                )
                            }
                            Text(
                                modifier = modifier.weight(1f),
                                text = trendingList[page].overview ?: "-",
                                style = MaterialTheme.textStyle.carouselOverview,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }

        TextButton(onClick = onViewAllClick) {
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

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            pagerState.animateScrollToPage(
                page = if (pagerState.currentPage == pagerState.pageCount - 1) 0 else pagerState.currentPage + 1,
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun MediaListPreview() {
    CineTheme {
        Surface {
            TrendingtopCarousel(
                trendingList = listOf(
                    MediaData(
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
                onViewAllClick = {},
            )
        }
    }
}