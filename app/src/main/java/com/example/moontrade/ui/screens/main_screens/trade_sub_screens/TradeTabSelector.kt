package com.example.moontrade.ui.screens.main_screens.trade_sub_screens

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.moontrade.ui.screens.main_screens.TradeTab

@Composable
fun TradeTabSelector(
    selectedTab: TradeTab,
    onTabSelected: (TradeTab) -> Unit
) {
    val tabs = listOf(TradeTab.BUY, TradeTab.SELL, TradeTab.EXCHANGE)
    TabRow(selectedTabIndex = tabs.indexOf(selectedTab)) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = { Text(tab.name) }
            )
        }
    }
}
