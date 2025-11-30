package com.example.moontrade.navigation

import MarketDetailScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moontrade.auth.AuthViewModel
import com.example.moontrade.ui.screens.authentication.*
import com.example.moontrade.ui.screens.components.bars.BottomBar
import com.example.moontrade.ui.screens.main_screens.*
import com.example.moontrade.ui.screens.onboarding.OnboardingPagerScreen
import com.example.moontrade.ui.screens.profile.PlayerProfileScreen
import com.example.moontrade.ui.screens.profile.UserProfileScreen
import com.example.moontrade.ui.theme.ThemeViewModel
import com.example.moontrade.viewmodels.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // ViewModels
    val authViewModel: AuthViewModel = hiltViewModel()
    val marketViewModel: MarketViewModel = hiltViewModel()
    val balanceViewModel: BalanceViewModel = hiltViewModel()
    val tournamentsViewModel: TournamentsViewModel = hiltViewModel()
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val marketDetailViewModel: MarketDetailViewModel = hiltViewModel()
    val selectedPlayerViewModel: SelectedPlayerViewModel = hiltViewModel()
    val leaderboardViewModel: LeaderboardViewModel = hiltViewModel()
    val userAssetsViewModel: UserAssetsViewModel = hiltViewModel()
    val ordersViewModel: OrdersViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val bottomBarRoutes = listOf(
        NavRoutes.HOME,
        NavRoutes.MARKETS,
        NavRoutes.RATINGS,
        NavRoutes.TOURNAMENTS,
        NavRoutes.PROFILE,
        NavRoutes.USER_ORDERS
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(NavRoutes.HOME) {
                popUpTo(0)
            }
        }
    }

    val content: @Composable (PaddingValues) -> Unit = { padding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.ONBOARDING,
            modifier = Modifier.padding(padding)
        ) {
            // ---------- ONBOARDING (pager: swipe + button) ----------
            composable(NavRoutes.ONBOARDING) {
                OnboardingPagerScreen(
                    onFinished = {
                        navController.navigate(NavRoutes.WELCOME) {
                            popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
                        }
                    },
                    onSkip = {
                        navController.navigate(NavRoutes.WELCOME) {
                            popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }
            // ---------- AUTH / WELCOME ----------
            composable(NavRoutes.WELCOME) {
                WelcomeScreen(navController)
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
            // ---------- HOME ----------
            composable(NavRoutes.HOME) {
                HomeScreen(
                    navController,
                    balanceViewModel,
                    tournamentsViewModel,
                    profileViewModel,
                    userAssetsViewModel,
                    leaderboardViewModel,
                    selectedPlayerViewModel
                )
            }
            // ---------- MARKETS ----------
            composable(NavRoutes.MARKETS) {
                MarketsScreen(navController, marketViewModel, themeViewModel)
            }
            // ---------- RATINGS ----------
            composable(NavRoutes.RATINGS) {
                RatingsScreen(
                    navController = navController,
                    leaderboardViewModel = leaderboardViewModel,
                    selectedPlayerViewModel = selectedPlayerViewModel,
                    profileViewModel = profileViewModel
                )
            }
            // ---------- TOURNAMENTS ----------
            composable(NavRoutes.TOURNAMENTS) {
                TournamentsScreen(navController, tournamentsViewModel)
            }
            // ---------- USER PROFILE ----------
            composable(NavRoutes.PROFILE) {
                UserProfileScreen(navController, profileViewModel)
            }
            // ---------- OTHER PLAYER PROFILE ----------
            composable(NavRoutes.PLAYER_PROFILE) {
                PlayerProfileScreen(navController, selectedPlayerViewModel)
            }
            // ---------- MARKET DETAIL ----------
            composable(
                route = "${NavRoutes.MARKET_DETAIL}/{symbol}",
                arguments = listOf(navArgument("symbol") { type = NavType.StringType })
            ) { backStackEntry ->
                val symbol = backStackEntry.arguments?.getString("symbol")
                MarketDetailScreen(
                    navController = navController,
                    symbol = symbol ?: "null",
                    marketDetailViewModel,
                    userAssetsViewModel,
                    balanceViewModel
                )
            }
            // ---------- SETTINGS ----------
            composable(NavRoutes.SETTINGS) {
                SettingsScreen(navController, authViewModel, themeViewModel)
            }
            // ---------- EDIT PROFILE ----------
            composable(NavRoutes.PROFILE_EDIT) {
                EditProfileScreen(navController, profileViewModel)
            }
            // ---------- USER ORDERS ----------
            composable(NavRoutes.USER_ORDERS) {
                OrdersScreen(navController, ordersViewModel)
            }
        }
    }
    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                BottomBar(navController)
            }
        }
    ) { padding ->
        content(padding)
    }
}
