package compose.base.app.presentation.screen.auth.forgot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.base.app.config.util.rememberLifecycleEvent
import compose.base.app.presentation.ui.theme.CineTheme
import compose.base.app.presentation.ui.theme.spacing

@ExperimentalMaterial3Api
@Composable
fun ForgotRoute(
    modifier: Modifier = Modifier,
    viewModel: ForgotViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.navigateToHome) {
        if (uiState.navigateToHome) navigateToHome()
    }

    if (rememberLifecycleEvent() == Lifecycle.Event.ON_STOP) {
        viewModel.onScreenFinish()
    }
    ForgotScreen(
        modifier = modifier,
        uiState = uiState,
        uiEvent = viewModel::handleEvent,
    )
}

@ExperimentalMaterial3Api
@Composable
fun ForgotScreen(
    modifier: Modifier = Modifier,
    uiState: ForgotUiState,
    uiEvent: (ForgotUiEvent) -> Unit,
) {
    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    var confirmPasswordVisibility by remember {
        mutableStateOf(false)
    }
    Scaffold { safeAreaPadding ->
        Column(
            modifier = modifier
                .padding(safeAreaPadding)
                .padding(horizontal = MaterialTheme.spacing.medium)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                value = uiState.password,
                onValueChange = {
                    uiEvent(ForgotUiEvent.OnPasswordChanged(it))
                },
                shape = MaterialTheme.shapes.medium,
                placeholder = { Text(text = "Password") },
                label = { Text(text = "Password") },
                trailingIcon = {
                    val image = if (passwordVisibility) Icons.Filled.VisibilityOff
                    else Icons.Filled.Visibility
                    val description = if (passwordVisibility) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(imageVector = image, description)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
            )

            Spacer(modifier = modifier.height(MaterialTheme.spacing.medium))

            OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                value = uiState.confirmPassword,
                onValueChange = {
                    uiEvent(ForgotUiEvent.OnConfirmPasswordChanged(it))
                },
                shape = MaterialTheme.shapes.medium,
                placeholder = { Text(text = "Confirm Password") },
                label = { Text(text = "Confirm Password") },
                trailingIcon = {
                    val image = if (confirmPasswordVisibility) Icons.Filled.VisibilityOff
                    else Icons.Filled.Visibility
                    IconButton(onClick = {
                        confirmPasswordVisibility = !confirmPasswordVisibility
                    }) {
                        Icon(imageVector = image, null)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
            )

            Spacer(modifier = modifier.height(MaterialTheme.spacing.medium))

            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = { uiEvent(ForgotUiEvent.OnSubmit) },
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "Create password")
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun ForgotScreenPreview() {
    CineTheme {
        ForgotScreen(
            uiState = ForgotUiState(),
            uiEvent = {},
        )
    }
}