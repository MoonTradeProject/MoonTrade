package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moontrade.model.TradeMatch
import com.example.moontrade.ui.screens.main_screens.OrdersScreen
import com.example.moontrade.ui.screens.main_screens.order_sub_screen.OrderRow
import com.example.moontrade.viewmodels.OrdersViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.filter
import kotlin.collections.takeLast

@Composable
fun TradeMatchesList(
    matches: List<TradeMatch>,
    selectedTab: Int,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit,
    navController: NavController,
    ordersViewModel: OrdersViewModel,
    symbol: String,
    modifier: Modifier = Modifier
) {
    val orders by ordersViewModel.orders.collectAsState()
    val filteredOrders = remember(orders, symbol) {
        orders.filter { order ->
            val isStatusActive = order.status.equals("partiallyFilled", ignoreCase = true) ||
                    order.status.equals("pending", ignoreCase = true) ||
                    order.status.equals("active", ignoreCase = true)

            val matchesSymbol = order.asset_name.equals(symbol, ignoreCase = true)
            print(orders)
            isStatusActive && matchesSymbol
        }
    }

    TabRow(
        selectedTabIndex = selectedTab,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.DarkGray.copy(alpha = 0.4f),
        contentColor = Color.White
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        title,
                        fontSize = 14.sp,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                    )
                },
                modifier = Modifier.background(
                    if (selectedTab == index) Color.Gray else Color.DarkGray.copy(alpha = 0.4f)
                )
            )
        }
    }

    when (selectedTab) {
        0 -> LazyColumn(
            modifier = modifier.fillMaxWidth(),
            reverseLayout = false
        ) {
            items(matches) { match ->
                TradeMatchRow(match)
            }
        }

        1 -> LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(filteredOrders) { order ->
                        OrderRow(
                            order = order,
                            onCancel = { ordersViewModel.cancelOrder(order.id) }
                        )
                    }
                }
        }
}

@Composable
fun TradeMatchRow(match: TradeMatch) {
    val color = if (match.side.lowercase() == "buy") Color(0xFF4CAF50)
    else Color(0xFFF44336)

    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val time = sdf.format(Date(match.timestamp))

    val volume = match.qty

// 2. Safely get the integer part of the amount (e.g., 12.345 -> 12)
    val integerPart = volume.toLong()

// 3. Get the length of the integer part string representation
    val integerPartLength = integerPart.toString().length

// The rest of your logic is now safe
    val adjustedDecimals = if (integerPartLength >= 3) 1 else 4
    val safeDecimals = adjustedDecimals.coerceIn(0, 8)
    val formattedVolume = "%.${safeDecimals}f".format(volume)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = match.price.toString(),
            color = color,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )


        Text(
            text = formattedVolume,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )


        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
    }
}
