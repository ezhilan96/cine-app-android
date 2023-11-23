package compose.base.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import compose.base.app.config.util.NetworkConnectivityObserver
import compose.base.app.config.util.NetworkConnectivityObserver.NetworkStatus
import compose.base.app.data.dataSource.local.preference.UserPreferencesDataStore
import compose.base.app.presentation.pages.NoInternetScreen
import compose.base.app.presentation.pages.SplashScreen
import compose.base.app.presentation.pages.home.HomeRoute
import compose.base.app.presentation.ui.theme.CineTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStore: UserPreferencesDataStore

    @Inject
    lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            CineTheme {
                val networkStatus by networkConnectivityObserver.observe()
                    .collectAsState(initial = NetworkStatus.UnAvailable)
                val modifier = Modifier
                when (networkStatus) {

                    NetworkStatus.Lost -> NoInternetScreen(modifier = modifier)

                    else -> {
                        val isLoggedIn by dataStore.isLoggedIn.collectAsState(initial = null)
                        if (isLoggedIn != null && networkStatus == NetworkStatus.Available) {
                            val navController = rememberNavController()
                            NavHost(
                                modifier = modifier.fillMaxSize(),
                                navController = navController,
                                startDestination = if (isLoggedIn!!) MainRoutes.HomeScreen.route
                                else MainRoutes.HomeScreen.route
                            ) {
                                composable(route = MainRoutes.HomeScreen.route) {
                                    HomeRoute(
                                        modifier = modifier,
                                        navController = navController,
                                    )
                                }
                            }
                        } else SplashScreen(modifier = modifier)
                    }
                }
            }
        }
    }
}

sealed class MainRoutes(val route: String) {

    object HomeScreen : MainRoutes(
        route = "home"
    )

//    object DetailScreen : MainRoutes(
//        route = "detail"
//    ) {
//        const val idArg: String = "1"
//        val routeWithArgs: String = "${route}/{$idArg}"
//        val arguments = listOf(navArgument(idArg) { type = NavType.StringType },
//            navArgument(idArg) { type = NavType.StringType })
//    }

}