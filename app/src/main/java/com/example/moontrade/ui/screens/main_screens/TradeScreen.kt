package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.viewmodels.TradeViewModel
import com.example.moontrade.ui.screens.main_screens.trade_sub_screens.*

enum class TradeTab { BUY, SELL /* , EXCHANGE */ }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeScreen(navController: NavController) {


    val vm: TradeViewModel = hiltViewModel()

    var selectedTab by remember { mutableStateOf(TradeTab.BUY) }

    Scaffold(
        topBar = {
            TopBar(
                title     = "BTCUSDT",
                showBack  = true,
                onBack    = { navController.popBackStack() }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {


            val balance by vm.balance.collectAsState("0")
            BalanceHeader(
                userBalance      = balance.toDoubleOrNull() ?: 0.0,
                selectedCurrency = "USDT"
            )

            Spacer(Modifier.height(16.dp))

            TradeTabSelector(
                selectedTab   = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(Modifier.height(16.dp))

            when (selectedTab) {
                TradeTab.BUY  -> BuyTab(vm)
                TradeTab.SELL -> SellTab(vm)
                // TradeTab.EXCHANGE -> ExchangeTab(vm)
            }
        }
    }
}
