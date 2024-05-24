package com.ezhilan.cine.presentation.screens.home.dashboard.trending.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
        if (painter.state !is AsyncImagePainter.State.Success) Box(
            modifier = modifier.fillMaxSize()
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
                        .size(MaterialTheme.spacing.unit50),
                    painter = painterResource(id = R.drawable.ic_corrupt_image),
                    contentDescription = null,
                )
            }
        }
    }
}