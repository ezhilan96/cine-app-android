package compose.base.app.presentation.screen.auth.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.base.app.config.util.rememberLifecycleEvent
import compose.base.app.presentation.ui.theme.CineTheme
import compose.base.app.presentation.ui.theme.spacing

@ExperimentalMaterial3Api
@Composable
fun SignupRoute(
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = hiltViewModel(),
    navigateToHome: ()->Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.navigateToHome) {
        if (uiState.navigateToHome)
            navigateToHome()
    }

    if (rememberLifecycleEvent() == Lifecycle.Event.ON_STOP) {
        viewModel.onScreenFinish()
    }
    SignupScreen(
        modifier = modifier,
        uiState = uiState,
        uiEvent = viewModel::handleEvent,
    )
}

@ExperimentalMaterial3Api
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    uiState: SignupUiState,
    uiEvent: (SignupUiEvent) -> Unit,
) {
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
                value = uiState.userName,
                onValueChange = {
                    uiEvent(SignupUiEvent.OnUserNameChange(it))
                },
                shape = MaterialTheme.shapes.medium,
                placeholder = { Text(text = "Email or Phone number") },
                label = { Text(text = "Username") },
            )

            Spacer(modifier = modifier.height(MaterialTheme.spacing.medium))

            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = { uiEvent(SignupUiEvent.OnSubmit) },
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "Signup")
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun SignupScreenPreview() {
    CineTheme {
        SignupScreen(
            uiState = SignupUiState(),
            uiEvent = {},
        )
    }
}