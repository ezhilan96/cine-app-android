package com.ezhilan.cine.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ezhilan.cine.presentation.screens.home.dashboard.DashboardScreen
import com.ezhilan.cine.presentation.screens.routes.HomeRoute

@Composable
fun HomeNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navigateUp: () -> Unit = { navController.navigateUp() }
    NavHost(
        navController = navController,
        startDestination = HomeRoute.DashboardDestination.route,
    ) {
        composable(HomeRoute.DashboardDestination.route) {
            DashboardScreen()
        }

        composable(HomeRoute.MovieDestination.route) {

        }

        composable(HomeRoute.TvDestination.route) {

        }

        composable(HomeRoute.PeopleDestination.route) {

        }
    }
}