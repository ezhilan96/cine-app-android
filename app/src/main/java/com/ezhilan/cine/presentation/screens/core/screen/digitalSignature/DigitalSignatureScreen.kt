package com.ezhilan.cine.presentation.screens.core.screen.digitalSignature

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ezhilan.cine.R
import com.ezhilan.cine.core.Constants.DEFAULT_FILE_EXTENSION
import com.ezhilan.cine.presentation.config.CineTheme
import com.ezhilan.cine.presentation.config.customShapes
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.config.textStyle
import com.ezhilan.cine.presentation.screens.core.component.TopProgressIndicatorLight
import com.ezhilan.cine.presentation.screens.core.dialog.DefaultAlert
import com.ezhilan.cine.presentation.util.enableGesture
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController
import java.io.File
import java.io.FileOutputStream

@Composable
fun DigitalSignatureDestination(
    modifier: Modifier = Modifier,
    onResult: (String) -> Unit,
    navigateUp: () -> Unit,
    viewModel: DigitalSignatureViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DigitalSignatureScreen(
        modifier = modifier,
        uiState = uiState,
        uiEvent = viewModel::onUiEvent,
        navigateUp = navigateUp,
    )
    LaunchedEffect(uiState.navigationItems) {
        uiState.navigationItems.forEach { navigationItem ->
            when (navigationItem) {
                DigitalSignatureNavItem.Done -> onResult(uiState.signUrl!!)
                DigitalSignatureNavItem.AlertDialog -> {}
            }
        }
    }
    if (uiState.navigationItems.contains(DigitalSignatureNavItem.AlertDialog)) {
        DefaultAlert(
            message = uiState.alertMessage.asString(),
            onDismiss = { viewModel.onUiEvent(DigitalSignatureUiEvent.OnDismiss()) },
        )
    }
}

@Composable
fun DigitalSignatureScreen(
    modifier: Modifier = Modifier,
    uiState: DigitalSignatureUiState,
    uiEvent: (DigitalSignatureUiEvent) -> Unit,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    val drawController = rememberDrawController()
    drawController.changeColor(Color.Black)
    drawController.changeStrokeWidth(5f)
    Scaffold(modifier = modifier.enableGesture(!uiState.isLoading)) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    modifier = modifier.padding(
                        horizontal = MaterialTheme.spacing.unit4,
                        vertical = MaterialTheme.spacing.grid1
                    ),
                    onClick = navigateUp,
                ) {
                    Icon(
                        modifier = modifier.padding(MaterialTheme.spacing.grid1),
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = null,
                    )
                }
                Text(
                    modifier = modifier.padding(start = MaterialTheme.spacing.grid1),
                    text = stringResource(R.string.title_customer_signature),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            TopProgressIndicatorLight(
                modifier = modifier.fillMaxWidth(),
                isLoading = uiState.isLoading,
            )

            DrawBox(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f),
                drawController = drawController,
                bitmapCallback = { image, _ ->
                    val documentFile = File(
                        context.cacheDir,
                        System.currentTimeMillis().toString() + DEFAULT_FILE_EXTENSION
                    )
                    try {
                        val fileOutputStream = FileOutputStream(documentFile)
                        image?.asAndroidBitmap()?.compress(
                            Bitmap.CompressFormat.JPEG, 30, fileOutputStream
                        )
                        fileOutputStream.flush()
                        fileOutputStream.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    uiEvent(DigitalSignatureUiEvent.OnSubmit(documentFile.toUri()))
                },
            )
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.customShapes.defaultBottomSheet,
                    )
            ) {
                Row(
                    modifier = modifier
                        .padding(MaterialTheme.spacing.grid1)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(
                        onClick = {
                            drawController.reset()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.inverseOnSurface
                        ),
                        shape = MaterialTheme.customShapes.defaultButton,
                    ) {
                        Icon(
                            modifier = modifier.padding(end = MaterialTheme.spacing.grid1),
                            imageVector = Icons.Outlined.Replay,
                            contentDescription = null,
                        )
                        Text(
                            text = stringResource(R.string.button_reset),
                            style = MaterialTheme.textStyle.ubuntuLabel,
                        )
                    }
                    TextButton(
                        onClick = {
                            if (drawController.exportPath().path.isNotEmpty()) {
                                drawController.saveBitmap()
                            } else {
                                Toast.makeText(
                                    context, ContextCompat.getString(
                                        context, R.string.error_empty_signature
                                    ), Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.inverseOnSurface
                        ),
                        shape = MaterialTheme.customShapes.defaultButton,
                    ) {
                        Icon(
                            modifier = modifier.padding(end = MaterialTheme.spacing.grid1),
                            imageVector = Icons.Outlined.Done,
                            contentDescription = null,
                        )
                        Text(
                            text = stringResource(id = R.string.button_submit),
                            style = MaterialTheme.textStyle.ubuntuLabel,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SignaturePreview() {
    CineTheme {
        DigitalSignatureScreen(
            Modifier,
            DigitalSignatureUiState(isLoading = true),
            {},
            {},
        )
    }
}