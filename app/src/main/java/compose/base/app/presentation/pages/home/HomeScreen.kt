package compose.base.app.presentation.pages.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@ExperimentalMaterial3Api
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    HomeScreen(modifier = modifier)
}

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val navigationItems = listOf(
        HomeRoutes.DiscoverScreen,
        HomeRoutes.TrendingScreen,
        HomeRoutes.AccountScreen,
    )
    var navigationSelectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(bottomBar = {
        NavigationBar {
            navigationItems.forEachIndexed { index, navigationItem ->
                NavigationBarItem(
                    selected = navigationSelectedIndex == index,
                    onClick = {
                        navigationSelectedIndex = index
                        navController.navigate(navigationItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            navigationItem.icon,
                            contentDescription = null,
                        )
                    },
                )
            }
        }
    }) {
        it
        NavHost(
            navController = navController,
            startDestination = navigationItems[navigationSelectedIndex].route,
        ) {
            composable(route = HomeRoutes.DiscoverScreen.route) {

            }
        }
    }
}

sealed class HomeRoutes(
    val label: String, val route: String, val icon: ImageVector
) {

    object DiscoverScreen : HomeRoutes(
        label = "Discover",
        route = "discover",
        icon = Icons.Filled.Category,
    )

    object TrendingScreen : HomeRoutes(
        label = "Trending",
        route = "trending",
        icon = Icons.Filled.TrendingUp,
    )

    object AccountScreen : HomeRoutes(
        label = "Account",
        route = "account",
        icon = Icons.Filled.AccountCircle,
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

@ExperimentalMaterial3Api
@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(modifier = Modifier)
}