package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.*
import com.example.moontrade.ui.screens.components.bars.BottomBar
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.main_screens.trade_utils.BuyTab
import com.example.moontrade.ui.screens.main_screens.trade_utils.SellTab
import com.example.moontrade.ui.screens.main_screens.trade_utils.ExchangeTab
import com.example.moontrade.ui.screens.main_screens.trade_utils.BalanceHeader
import com.example.moontrade.ui.screens.main_screens.trade_utils.TradeTabSelector

enum class TradeTab { BUY, SELL, EXCHANGE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(TradeTab.BUY) }
    Scaffold(
        topBar = {
            TopBar(
                title = "Come and trade you pussy",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            BottomBar(navController)
        }

    )
    { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            BalanceHeader(
                userBalance = 5200.0,
                selectedCurrency = "USDT"
            )

            Spacer(Modifier.height(16.dp))

            // Tabs
            TradeTabSelector(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(Modifier.height(16.dp))

            when (selectedTab) {
                TradeTab.BUY -> BuyTab()
                TradeTab.SELL -> SellTab()
                TradeTab.EXCHANGE -> ExchangeTab()
            }
        }
    }
}
