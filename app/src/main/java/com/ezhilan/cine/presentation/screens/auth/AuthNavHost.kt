package com.ezhilan.cine.presentation.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ezhilan.cine.presentation.screens.routes.AuthRoute

@Composable
fun AuthNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navigateUp: () -> Unit = { navController.navigateUp() }
    NavHost(
        navController = navController,
        startDestination = AuthRoute.LoginDestination.route,
    ) {

        AuthRoute.LoginDestination.let { destination ->
            composable(route = destination.route) {
//                LoginDestination(
//                    modifier = modifier,
//                    navigateToOtp = {
//                        destination.navigateToOtp(navController, it)
//                    },
//                )
            }
        }

        AuthRoute.OtpDestination.let { destination ->
            composable(
                route = destination.routeWithArgs,
                arguments = destination.arguments,
            ) { navBackStackEntry ->
//                val phoneNumber =
//                    remember { navBackStackEntry.arguments?.getString(destination.phoneArg)!! }
//                val otpViewModel = hiltViewModel<OtpViewModel>()
//                OtpDestination(
//                    modifier = modifier,
//                    navigateUp = navigateUp,
//                    viewModel = otpViewModel,
//                )
//                LaunchedEffect(Unit) {
//                    otpViewModel.setPhoneNumber(phoneNumber)
//                }
            }
        }
    }
}