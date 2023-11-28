package compose.base.app.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import compose.base.app.config.util.navigateClearingStack
import compose.base.app.presentation.core.NoInternetScreen
import compose.base.app.presentation.core.SplashScreen
import compose.base.app.presentation.screen.auth.forgot.ForgotRoute
import compose.base.app.presentation.screen.auth.login.LoginRoute
import compose.base.app.presentation.screen.auth.signup.SignupRoute
import compose.base.app.presentation.screen.home.HomeScreen

@ExperimentalMaterial3Api
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val networkStatus by mainViewModel.connectionState.collectAsStateWithLifecycle()
    val isLoggedIn by mainViewModel.loginState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navigateToHome = {
        navController.navigateClearingStack(MainRoutes.Home.route)
    }
    LaunchedEffect(isLoggedIn) {
        isLoggedIn?.let {
            if (it) {
                navigateToHome()
            } else {
                navController.navigateClearingStack(MainRoutes.Auth.route)
            }
        }
    }
    Scaffold {
        if (networkStatus == ConnectionState.Disconnected) {
            NoInternetScreen(modifier = modifier.padding(it))
        } else {
            NavHost(
                modifier = modifier
                    .padding(it)
                    .fillMaxSize(),
                navController = navController,
                startDestination = MainRoutes.SplashScreen.route,
            ) {
                composable(route = MainRoutes.SplashScreen.route) {
                    SplashScreen(modifier = modifier)
                }

                navigation(
                    route = MainRoutes.Auth.route,
                    startDestination = AuthRoutes.LoginScreen.route,
                ) {
                    composable(route = AuthRoutes.LoginScreen.route) {
                        LoginRoute(
                            modifier = modifier,
                            navigateToHome = navigateToHome,
                            navigateToSignUp = {
                                navController.navigate(AuthRoutes.SignUpScreen.route)
                            },
                            navigateToForgot = {
                                navController.navigate(AuthRoutes.ForgotScreen.route)
                            },
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
                    HomeScreen(modifier = modifier)
                }
            }
        }
    }
}


sealed class MainRoutes(val route: String) {

    data object SplashScreen : MainRoutes(route = "splash")

    data object Home : MainRoutes(route = "home")

    data object Auth : MainRoutes(route = "auth")
}

sealed class AuthRoutes(val route: String) {

    data object LoginScreen : AuthRoutes(route = "login")

    data object SignUpScreen : AuthRoutes(route = "sign_up")

    data object ForgotScreen : AuthRoutes(route = "forgot")
}