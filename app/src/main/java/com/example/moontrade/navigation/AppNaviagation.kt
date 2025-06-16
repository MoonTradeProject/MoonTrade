package com.example.moontrade.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.moontrade.auth.AuthViewModel
import com.example.moontrade.ui.screens.authentication.*
import com.example.moontrade.ui.screens.components.bars.BottomBar
import com.example.moontrade.ui.screens.main_screens.*
import com.example.moontrade.ui.screens.onboarding.*
import com.example.moontrade.ui.screens.profile.PlayerProfeleScreen
import com.example.moontrade.ui.theme.ThemeViewModel
import com.example.moontrade.viewmodels.BalanceViewModel
import com.example.moontrade.viewmodels.MarketViewModel
import com.example.moontrade.viewmodels.ProfileViewModel
import com.example.moontrade.viewmodels.TournamentsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val marketViewModel: MarketViewModel = hiltViewModel()
    val balanceViewModel: BalanceViewModel = hiltViewModel()
    val tournamentsViewModel: TournamentsViewModel = hiltViewModel()
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(NavRoutes.HOME) {
                popUpTo(0)
            }
        }
    }


    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->
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
                HomeScreen(navController, balanceViewModel, tournamentsViewModel, profileViewModel)
            }
            composable(NavRoutes.MARKETS) {
                MarketsScreen(navController, marketViewModel)
            }

            composable(NavRoutes.RATINGS) {
                RatingsScreen(navController)
            }
            composable(NavRoutes.TOURNAMENTS) {
                TournamentsScreen(navController, tournamentsViewModel)
            }
            composable(NavRoutes.PLAYER_PROFILE) { backStackEntry ->
                val playerId = backStackEntry.arguments?.getString("playerId") ?: ""
                PlayerProfeleScreen(navController, playerId)
            }
            composable(
                route = "${NavRoutes.MARKET_DETAIL}/{symbol}",
                arguments = listOf(navArgument("symbol") { type = NavType.StringType })
            ) { backStackEntry ->
                val symbol = backStackEntry.arguments?.getString("symbol")
                println("🟡 ROUTE ACTIVE — symbol = $symbol")
                MarketDetailScreen(navController = navController, symbol = symbol ?: "null")
            }
            composable(NavRoutes.SETTINGS) {
                SettingsScreen(navController, authViewModel, themeViewModel)
            }
            composable(NavRoutes.PROFILE_EDIT) {
                EditProfileScreen(navController, profileViewModel)
            }

        }
    }
}
