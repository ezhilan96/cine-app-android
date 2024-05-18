package com.ezhilan.cine.presentation.screens.routes

import com.ezhilan.cine.core.Constants

sealed class MainRoute(val route: String) {

    data object SplashDestination : MainRoute(route = Constants.ROUTE_SPLASH)

    data object AuthRoute : MainRoute(route = Constants.ROUTE_AUTH)

    data object HomeRoute : MainRoute(route = Constants.ROUTE_HOME)
}