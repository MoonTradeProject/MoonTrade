package com.example.moontrade.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moontrade.auth.AuthViewModel
import com.example.moontrade.ui.screens.authentication.*
import com.example.moontrade.ui.screens.main_screens.*
import com.example.moontrade.ui.screens.onboarding.*
import com.example.moontrade.ui.screens.profile.PlayerProfeleScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.ONBOARDING_1,
            modifier = Modifier.padding(padding)
        ) {
            composable(NavRoutes.ONBOARDING_1) {
                OnboardingOne(onNext = { navController.navigate(NavRoutes.ONBOARDING_2) })
            }
            composable(NavRoutes.ONBOARDING_2) {
                OnboardingTwo(onNext = { navController.navigate(NavRoutes.ONBOARDING_3) })
            }
            composable(NavRoutes.ONBOARDING_3) {
                OnboardingThree(onNext = { navController.navigate(NavRoutes.WELCOME) })
            }
            composable(NavRoutes.WELCOME) {
                WelcomeScreen(navController, authViewModel)
            }
            composable(NavRoutes.ACCOUNT_CREATION) {
                AccountCreationScreen(navController)
            }
            composable(NavRoutes.ADD_MAIL) {
                AddMail(navController, authViewModel)
            }
            composable(NavRoutes.CONFIRM_MAIL) {
                ConfirmEmail(navController, authViewModel)
            }
            composable(NavRoutes.ENTER_CODE) {
                EnterCode(navController, authViewModel)
            }
            composable(NavRoutes.CREATE_PASSWORD) {
                CreatePassword(navController, authViewModel)
            }
            composable(NavRoutes.SUCCESSFUL_REGISTRATION) {
                SuccessfulRegistration(navController)
            }
            composable(NavRoutes.LOGIN) {
                LoginScreen(navController, authViewModel)
            }
            composable(NavRoutes.HOME) {
                HomeScreen(navController)
            }
            composable(NavRoutes.MARKETS) {
                MarketsScreen(navController)
            }
            composable(NavRoutes.TRADE) {
                TradeScreen(navController)
            }
            composable(NavRoutes.RATINGS) {
                RatingsScreen(navController)
            }
            composable(NavRoutes.TOURNAMENTS) {
                TournamentsScreen(navController)
            }
            composable(NavRoutes.PLAYER_PROFILE) { backStackEntry ->
                val playerId = backStackEntry.arguments?.getString("playerId") ?: ""
                PlayerProfeleScreen(navController, playerId)
            }
        }
    }
}
