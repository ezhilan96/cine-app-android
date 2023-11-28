package compose.base.app.presentation.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import compose.base.app.presentation.screen.home.account.AccountRoute
import compose.base.app.presentation.screen.home.discover.DiscoverRoute
import compose.base.app.presentation.screen.home.trending.TrendingRoute

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            MainNavBar(navController = navController)
        },
    ) {
        NavHost(
            modifier = modifier
                .padding(it)
                .fillMaxSize(),
            navController = navController,
            startDestination = HomeRoutes.DiscoverScreen.route,
        ) {
            composable(route = HomeRoutes.DiscoverScreen.route) {
                DiscoverRoute(modifier = modifier)
            }
            composable(route = HomeRoutes.TrendingScreen.route) {
                TrendingRoute(modifier = modifier)
            }
            composable(route = HomeRoutes.AccountScreen.route) {
                AccountRoute(modifier = modifier)
            }
        }
    }
}

@Composable
fun MainNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar {
        navigationItems.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                label = { Text(text = navigationItems[index].label) },
                icon = {
                    Icon(
                        if (currentRoute == navigationItem.route) navigationItem.selectedIcon
                        else navigationItem.unSelectedIcon,
                        contentDescription = null,
                    )
                },
                selected = currentRoute == navigationItem.route,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(navigationItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

sealed class HomeRoutes(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
) {

    data object DiscoverScreen : HomeRoutes(
        label = "Discover",
        route = "discover",
        selectedIcon = Icons.Filled.Category,
        unSelectedIcon = Icons.Outlined.Category,
    )

    data object TrendingScreen : HomeRoutes(
        label = "Trending",
        route = "trending",
        selectedIcon = Icons.Filled.TrendingUp,
        unSelectedIcon = Icons.Outlined.TrendingUp,
    )

    data object AccountScreen : HomeRoutes(
        label = "Account",
        route = "account",
        selectedIcon = Icons.Filled.AccountCircle,
        unSelectedIcon = Icons.Outlined.AccountCircle,
    )
}

val navigationItems = listOf(
    HomeRoutes.DiscoverScreen,
    HomeRoutes.TrendingScreen,
    HomeRoutes.AccountScreen,
)

@ExperimentalMaterial3Api
@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(modifier = Modifier)
}