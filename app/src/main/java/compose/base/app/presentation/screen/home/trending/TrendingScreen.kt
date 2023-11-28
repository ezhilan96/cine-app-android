package compose.base.app.presentation.screen.home.trending

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.base.app.config.util.rememberLifecycleEvent
import compose.base.app.presentation.ui.theme.CineTheme

@ExperimentalMaterial3Api
@Composable
fun TrendingRoute(
    modifier: Modifier = Modifier,
    viewModel: TrendingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.navigateToHome) {

    }
    if (rememberLifecycleEvent() == Lifecycle.Event.ON_STOP) viewModel.onScreenFinish()
    TrendingScreen(
        modifier = modifier,
        uiState = uiState,
        uiEvent = viewModel::handleEvent,
    )
}

@ExperimentalMaterial3Api
@Composable
fun TrendingScreen(
    modifier: Modifier = Modifier,
    uiState: TrendingUiState,
    uiEvent: (TrendingUiEvent) -> Unit,
) {
    Scaffold {
        it

    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun TrendingScreenPreview() {
    CineTheme {
        TrendingScreen(uiState = TrendingUiState(), uiEvent = {})
    }
}