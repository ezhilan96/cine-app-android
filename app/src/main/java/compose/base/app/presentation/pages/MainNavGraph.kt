package compose.base.app.presentation.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import compose.base.app.presentation.core.NoInternetScreen
import compose.base.app.presentation.core.SplashScreen
import compose.base.app.presentation.pages.ConnectionState.Connected
import compose.base.app.presentation.pages.ConnectionState.Disconnected
import compose.base.app.presentation.pages.auth.forgot.ForgotRoute
import compose.base.app.presentation.pages.auth.login.LoginRoute
import compose.base.app.presentation.pages.auth.signup.SignupRoute
import compose.base.app.presentation.pages.home.HomeRoute

@ExperimentalMaterial3Api
@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val networkStatus by mainViewModel.connectionState.collectAsStateWithLifecycle()
    val isLoggedIn by mainViewModel.loginState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navigateToHome = {
        navController.navigate(MainRoutes.Home.route) {
            popUpTo(MainRoutes.Auth.route) {
                inclusive = true
            }
        }
    }

    LaunchedEffect(networkStatus, isLoggedIn) {
        when {
            (networkStatus == Connected) -> if (isLoggedIn == true) {
                navigateToHome()
            } else {
                navController.navigate(MainRoutes.Auth.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }

            networkStatus == Disconnected -> navController.navigate(MainRoutes.NoInternetScreen.route)
        }
    }

    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = MainRoutes.SplashScreen.route,
    ) {
        composable(route = MainRoutes.SplashScreen.route) {
            SplashScreen(modifier = modifier)
        }

        composable(route = MainRoutes.NoInternetScreen.route) {
            NoInternetScreen(modifier = modifier)
        }

        navigation(
            route = MainRoutes.Auth.route,
            startDestination = AuthRoutes.LoginScreen.route,
        ) {
            composable(route = AuthRoutes.LoginScreen.route) {
                LoginRoute(
                    modifier = modifier,
                    navController = navController,
                    navigateToHome = navigateToHome,
                )
            }

            composable(route = AuthRoutes.SignUpScreen.route) {
                SignupRoute(
                    modifier = modifier,
                    navigateToHome = navigateToHome,
                )
            }

            composable(route = AuthRoutes.ForgotScreen.route) {
                ForgotRoute(
                    modifier = modifier,
                    navigateToHome = navigateToHome,
                )
            }
        }

        composable(route = MainRoutes.Home.route) {
            HomeRoute(
                modifier = modifier,
                navController = navController,
            )
        }
    }
}


sealed class MainRoutes(val route: String) {

    object SplashScreen : MainRoutes(route = "splash")

    object NoInternetScreen : MainRoutes(route = "noInternet")

    object Home : MainRoutes(route = "home")

    object Auth : MainRoutes(route = "auth")
}

sealed class AuthRoutes(val route: String) {

    object LoginScreen : AuthRoutes(route = "login")

    object SignUpScreen : AuthRoutes(route = "sign_up")

    object ForgotScreen : AuthRoutes(route = "forgot")
}