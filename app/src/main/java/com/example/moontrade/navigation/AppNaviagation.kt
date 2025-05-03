package com.example.moontrade.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moontrade.ui.screens.authentication.*
import com.example.moontrade.ui.screens.main_screens.HomeScreen
import com.example.moontrade.ui.screens.main_screens.MarketsScreen
import com.example.moontrade.ui.screens.main_screens.RatingsScreen
import com.example.moontrade.ui.screens.main_screens.TournamentsScreen
import com.example.moontrade.ui.screens.main_screens.TradeScreen
import com.example.moontrade.ui.screens.onboarding.OnboardingOne
import com.example.moontrade.ui.screens.onboarding.OnboardingTwo
import com.example.moontrade.ui.screens.onboarding.OnboardingThree
import com.example.moontrade.ui.screens.profile.PlayerProfeleScreen
import com.example.moontrade.auth.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = "onboarding1",
            modifier = Modifier.padding(padding)
        ) {
            composable("onboarding1") {
                OnboardingOne(onNext = { navController.navigate("onboarding2") })
            }
            composable("onboarding2") {
                OnboardingTwo(onNext = { navController.navigate("onboarding3") })
            }
            composable("onboarding3") {
                OnboardingThree(onNext = { navController.navigate("welcome") })
            }
            composable("welcome") {
                WelcomeScreen(navController, authViewModel)
            }
            composable("account_creation") {
                AccountCreationScreen(navController)
            }
            composable("add_mail") {
                AddMail(navController, authViewModel)
            }
            composable("confirm_mail") {
                ConfirmEmail(navController, authViewModel)
            }
            composable("enter_code") {
                EnterCode(navController, authViewModel)
            }
            composable("create_password") {
                CreatePassword(navController, authViewModel)
            }
            composable("successful_registration") {
                SuccessfulRegistration(navController)
            }
            composable("login") {
                LoginScreen(navController, authViewModel)
            }
            composable("home") { HomeScreen(navController) }
            composable("markets") { MarketsScreen(navController) }
            composable("trade") { TradeScreen(navController) }
            composable("ratings") { RatingsScreen(navController) }
            composable("tournaments") { TournamentsScreen(navController) }
            composable("player_profile/{playerId}") { backStackEntry ->
                val playerId = backStackEntry.arguments?.getString("playerId") ?: ""
                PlayerProfeleScreen(navController, playerId)
            }
        }
    }
}
