package com.ezhilan.cine.presentation.screens.home.dashboard.trending.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.presentation.config.CineTheme
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.config.textStyle
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.components.ProfileImage

@Composable
fun ProfileList(
    modifier: Modifier = Modifier,
    trendingList: List<MediaData>,
) {
    val localDensity = LocalDensity.current
    var screenWidth by remember { mutableStateOf(0.dp) }
    var maxItemWidth by remember { mutableStateOf(0.dp) }
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                screenWidth = with(localDensity) { coordinates.size.width.toDp() }
            },
        verticalAlignment = Alignment.Top,
    ) {
        trendingList.forEach { trendingData ->
            item { Spacer(modifier = modifier.width(MaterialTheme.spacing.grid1)) }
            item {
                Column {
                    Surface(
                        modifier = modifier
                            .height((screenWidth / 16) * 9)
                            .onGloballyPositioned { coordinates ->
                                maxItemWidth =
                                    with(localDensity) { coordinates.size.width.toDp() }
                            },
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small,
                    ) {
                        ProfileImage(
                            imagePath = trendingData.profilePath ?: ""
                        )
                    }

                    Spacer(modifier = modifier.height(MaterialTheme.spacing.grid05))

                    Text(
                        modifier = modifier.width(maxItemWidth),
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
        }
        item { Spacer(modifier = modifier.width(MaterialTheme.spacing.grid1)) }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun MediaListPreview() {
    CineTheme {
        Surface {
            ProfileList(
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
                        peopleType = "Actor"
                    ), MediaData(
                        id = "",
                        title = "Title\ntitle",
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