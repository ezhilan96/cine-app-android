package com.ezhilan.cine.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ezhilan.cine.presentation.screens.auth.AuthNavHost
import com.ezhilan.cine.presentation.screens.core.screen.SplashScreen
import com.ezhilan.cine.presentation.screens.home.HomeNavHost
import com.ezhilan.cine.presentation.screens.routes.MainRoute
import com.ezhilan.cine.presentation.util.navigateClearingStack

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    sessionState: SessionState,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainRoute.SplashDestination.route,
    ) {

        composable(route = MainRoute.SplashDestination.route) {
            SplashScreen(modifier = modifier)
        }

        composable(route = MainRoute.AuthRoute.route) {
            AuthNavHost(modifier = modifier)
        }

        composable(route = MainRoute.HomeRoute.route) {
            HomeNavHost(modifier = modifier)
        }
    }

    LaunchedEffect(sessionState) {
        when (sessionState) {

            SessionState.ACTIVE -> {
                navController.navigateClearingStack(MainRoute.HomeRoute.route)
            }

            SessionState.EXPIRED -> {
                navController.navigateClearingStack(MainRoute.AuthRoute.route)
            }

            else -> {}
        }
    }
}