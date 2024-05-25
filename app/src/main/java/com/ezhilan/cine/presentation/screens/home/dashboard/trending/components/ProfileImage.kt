package com.ezhilan.cine.presentation.screens.home.dashboard.trending.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.ezhilan.cine.R
import com.ezhilan.cine.presentation.config.spacing

@Composable
fun ProfileImage(modifier: Modifier = Modifier, imagePath: String) {
    val painter = rememberAsyncImagePainter(
        stringResource(R.string.image_base_url, imagePath)
    )
    Box(
        modifier
            .fillMaxHeight()
            .aspectRatio(2f / 3f)
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
                            .fillMaxSize()
                            .padding(MaterialTheme.spacing.grid3),
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}