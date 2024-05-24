@file:OptIn(ExperimentalMaterial3Api::class)

package com.ezhilan.cine.presentation.screens.home.dashboard.trending.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ezhilan.cine.R
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.domain.entity.TrendingData
import com.ezhilan.cine.presentation.config.CineTheme
import com.ezhilan.cine.presentation.config.colors
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.config.textStyle
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.components.PosterImage
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.components.ProfileImage
import java.util.Locale

@Composable
fun TrendingListDialog(
    modifier: Modifier = Modifier,
    trendingList: List<TrendingData>,
    mediaType: MediaType,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(modifier = modifier.fillMaxSize()) {
            Column(modifier = modifier.fillMaxSize()) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    title = {
                        Text(
                            text = when (mediaType) {
                                MediaType.all -> "All trending"
                                MediaType.movie -> "Trending Movies"
                                MediaType.tv -> "Trending TV shows"
                                MediaType.person -> "Trending People"
                            },
                        )
                    },
                )

                LazyVerticalGrid(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = MaterialTheme.spacing.grid05),
                    columns = GridCells.Fixed(3),
                ) {
                    trendingList.forEach { trendingData ->
                        item {
                            if (mediaType == MediaType.person) {
                                PeopleItemView(
                                    modifier = modifier,
                                    trendingData = trendingData,
                                )
                            } else {
                                MediaItemView(
                                    modifier = modifier,
                                    trendingData = trendingData,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MediaItemView(modifier: Modifier = Modifier, trendingData: TrendingData) {
    Column(
        modifier = modifier
            .padding(MaterialTheme.spacing.grid05)
            .padding(bottom = MaterialTheme.spacing.grid05)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.small,
        ) {
            Box {
                PosterImage(
                    imagePath = trendingData.posterPath ?: ""
                )
                Row(
                    modifier = modifier
                        .align(Alignment.TopStart)
                        .padding(MaterialTheme.spacing.grid05)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = modifier
                            .width(IntrinsicSize.Min)
                            .background(
                                color = MaterialTheme.colorScheme.inverseSurface,
                                shape = MaterialTheme.shapes.extraSmall,
                            )
                            .padding(horizontal = MaterialTheme.spacing.unit4)
                            .padding(vertical = MaterialTheme.spacing.unit2),
                        text = trendingData.mediaType.toString()
                            .replaceFirstChar { it.titlecase(Locale.ROOT) },
                        style = MaterialTheme.textStyle.trendingCardMediaType,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                    )
                }
            }
        }

        Spacer(modifier = modifier.height(MaterialTheme.spacing.grid05))

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = trendingData.releaseYear ?: "-",
                style = MaterialTheme.textStyle.trendingCardYear,
            )
            Spacer(modifier = modifier.width(MaterialTheme.spacing.grid05))

            Row(
                modifier = modifier.alignByBaseline(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = modifier.size(MaterialTheme.spacing.unit10),
                    painter = painterResource(id = R.drawable.ic_rating),
                    contentDescription = null,
                    tint = MaterialTheme.colors.star_yellow,
                )
                Spacer(modifier = modifier.width(MaterialTheme.spacing.grid05))
                Text(
                    text = trendingData.rating ?: "-",
                    style = MaterialTheme.textStyle.trendingCardRating,
                    maxLines = 1,
                )
            }
        }

        Spacer(modifier = modifier.height(MaterialTheme.spacing.grid05))

        Text(
            modifier = modifier.fillMaxWidth(),
            text = trendingData.title,
            style = MaterialTheme.textStyle.trendingCardTitle,
        )
    }
}

@Composable
fun PeopleItemView(modifier: Modifier = Modifier, trendingData: TrendingData) {
    Column(
        modifier = modifier
            .padding(MaterialTheme.spacing.grid05)
            .padding(bottom = MaterialTheme.spacing.grid05)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.small,
        ) {
            ProfileImage(
                imagePath = trendingData.profilePath ?: ""
            )
        }

        Spacer(modifier = modifier.height(MaterialTheme.spacing.grid05))

        Text(
            modifier = modifier.fillMaxWidth(),
            text = trendingData.title,
            style = MaterialTheme.textStyle.trendingCardTitle,
        )
        Spacer(modifier = modifier.height(MaterialTheme.spacing.grid05))

        Text(
            text = trendingData.peopleType ?: "-",
            style = MaterialTheme.textStyle.trendingCardYear,
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun AllTrendingListPreview() {
    CineTheme {
        Scaffold {
            TrendingListDialog(
                trendingList = listOf(
                    TrendingData(
                        id = "",
                        title = "Title",
                        overview = "Overview",
                        backdropPath = "/u1CqlLecfpcuOaugKi3ol9gDQHJ.jpg",
                        posterPath = "/vlHJfLsduZiILN8eYdN57kHZTcQ.jpg",
                        releaseYear = "2022",
                        mediaType = MediaType.tv,
                        genres = listOf(),
                        rating = "7.8",
                    ),
                    TrendingData(
                        id = "",
                        title = "Title",
                        overview = "Overview",
                        backdropPath = "/u1CqlLecfpcuOaugKi3ol9gDQHJ.jpg",
                        posterPath = "/vlHJfLsduZiILN8eYdN57kHZTcQ.jpg",
                        releaseYear = "2022",
                        mediaType = MediaType.tv,
                        genres = listOf(),
                        rating = "7.8",
                    ),
                    TrendingData(
                        id = "",
                        title = "Title",
                        overview = "Overview",
                        backdropPath = "/u1CqlLecfpcuOaugKi3ol9gDQHJ.jpg",
                        posterPath = "/vlHJfLsduZiILN8eYdN57kHZTcQ.jpg",
                        releaseYear = "2022",
                        mediaType = MediaType.tv,
                        genres = listOf(),
                        rating = "7.8",
                    ),
                    TrendingData(
                        id = "",
                        title = "Title",
                        overview = "Overview",
                        backdropPath = "/u1CqlLecfpcuOaugKi3ol9gDQHJ.jpg",
                        posterPath = "/vlHJfLsduZiILN8eYdN57kHZTcQ.jpg",
                        releaseYear = "2022",
                        mediaType = MediaType.tv,
                        genres = listOf(),
                        rating = "7.8",
                    ),
                ),
                mediaType = MediaType.person,
                onDismiss = {},
            )
        }
    }
}