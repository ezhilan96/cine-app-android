package com.ezhilan.cine.presentation.screens.routes

import androidx.annotation.DrawableRes
import com.ezhilan.cine.R
import com.ezhilan.cine.core.Constants

sealed class HomeRoute(val route: String) {
    data object DashboardDestination : HomeRoute(route = Constants.ROUTE_DASHBOARD)
    data object MovieDestination : HomeRoute(route = Constants.ROUTE_MOVIE)
    data object TvDestination : HomeRoute(route = Constants.ROUTE_TV)
    data object PeopleDestination : HomeRoute(route = Constants.ROUTE_PEOPLE)
}

sealed class DashboardRoute(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,
) {

    data object TrendingDestination :
        DashboardRoute(
            route = Constants.ROUTE_TRENDING,
            label = "Trending",
            icon = R.drawable.ic_trending
        )

    data object DiscoverDestination :
        DashboardRoute(
            route = Constants.ROUTE_DISCOVER,
            label = "Discover",
            icon = R.drawable.ic_discover
        )

    data object SettingsDestination :
        DashboardRoute(
            route = Constants.ROUTE_SETTINGS,
            label = "Settings",
            icon = R.drawable.ic_settings
        )
}


val dashboardMenuItems= listOf(
    DashboardRoute.TrendingDestination,
    DashboardRoute.DiscoverDestination,
    DashboardRoute.SettingsDestination
)