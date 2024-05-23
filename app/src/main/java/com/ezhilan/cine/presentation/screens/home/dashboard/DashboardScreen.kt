package com.ezhilan.cine.presentation.screens.home.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ezhilan.cine.presentation.screens.home.dashboard.settings.SettingsDestination
import com.ezhilan.cine.presentation.screens.home.dashboard.trending.TrendingDestination
import com.ezhilan.cine.presentation.screens.routes.DashboardRoute
import com.ezhilan.cine.presentation.screens.routes.dashboardMenuItems

@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var navBarSelectedIndex by remember {
        mutableIntStateOf(0)
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                dashboardMenuItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = navBarSelectedIndex == index,
                        onClick = {
                            navBarSelectedIndex = index
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = null,
                            )
                        },
                        label = { Text(text = item.label) },
                        alwaysShowLabel = false,
                    )
                }
            }
        },
    ) { safeAreaPadding ->
        NavHost(
            modifier = modifier.padding(safeAreaPadding),
            navController = navController,
            startDestination = DashboardRoute.TrendingDestination.route,
        ) {
            composable(DashboardRoute.TrendingDestination.route) {
                TrendingDestination(modifier = modifier)
            }
            composable(DashboardRoute.DiscoverDestination.route) {

            }
            composable(DashboardRoute.SettingsDestination.route) {
                SettingsDestination(modifier = modifier)
            }
        }
    }
}