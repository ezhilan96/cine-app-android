package compose.base.app.presentation.pages.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import compose.base.app.config.util.rememberLifecycleEvent
import compose.base.app.presentation.pages.AuthRoutes
import compose.base.app.presentation.ui.theme.CineTheme
import compose.base.app.presentation.ui.theme.colors
import compose.base.app.presentation.ui.theme.spacing

@ExperimentalMaterial3Api
@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController,
    navigateToHome: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState) {
        when {
            uiState.navigateToHome -> navigateToHome()
            uiState.navigateToSignup -> navController.navigate(AuthRoutes.SignUpScreen.route)
            uiState.navigateToForgot -> navController.navigate(AuthRoutes.ForgotScreen.route)
        }
    }
    if (rememberLifecycleEvent() == Lifecycle.Event.ON_STOP) viewModel.onScreenFinish()
    LoginScreen(
        modifier = modifier,
        uiState = uiState,
        uiEvent = viewModel::handleEvent,
    )
}

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    uiEvent: (LoginUiEvent) -> Unit,
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
                    uiEvent(LoginUiEvent.OnUserNameChange(it))
                },
                shape = MaterialTheme.shapes.medium,
                placeholder = { Text(text = "Email or Phone number") },
                label = { Text(text = "Username") },
            )

            Spacer(modifier = modifier.height(MaterialTheme.spacing.medium))

            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = { uiEvent(LoginUiEvent.OnSubmit) },
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "Login")
            }

            TextButton(
                modifier = modifier.fillMaxWidth(),
                onClick = { uiEvent(LoginUiEvent.OnForgotPasswordClicked) },
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "Forgot password?")
            }

            Box {
                Divider(modifier.padding(MaterialTheme.spacing.medium))
                Text(
                    modifier = modifier
                        .align(Alignment.Center)
                        .wrapContentSize()
                        .background(color = MaterialTheme.colors.textLight)
                        .padding(horizontal = MaterialTheme.spacing.small),
                    text = "New to Cine?",
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = { uiEvent(LoginUiEvent.OnSignUpClicked) },
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
fun LoginScreenPreview() {
    CineTheme {
        LoginScreen(
            uiState = LoginUiState(),
            uiEvent = {},
        )
    }
}