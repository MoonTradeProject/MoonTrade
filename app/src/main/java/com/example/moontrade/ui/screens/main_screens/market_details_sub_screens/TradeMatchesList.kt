package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moontrade.model.TradeMatch
import com.example.moontrade.ui.screens.main_screens.OrdersScreen
import com.example.moontrade.ui.screens.main_screens.order_sub_screen.OrderRow
import com.example.moontrade.viewmodels.OrdersViewModel
import com.example.moontrade.viewmodels.UserAssetsViewModel
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
    userAssetsViewModel: UserAssetsViewModel,
    modifier: Modifier = Modifier
) {
    val orders by ordersViewModel.orders.collectAsState()
    val filteredOrders = remember(orders, symbol) {
        orders.filter { order ->
            val isStatusActive = order.status.equals("partiallyFilled", ignoreCase = true) ||
                    order.status.equals("pending", ignoreCase = true) ||
                    order.status.equals("active", ignoreCase = true) ||
                    order.exec_type.equals("limit", ignoreCase = true)

            val matchesSymbol = order.asset_name.equals(symbol, ignoreCase = true)
            print(orders)
            isStatusActive && matchesSymbol
        }
    }

    TabRow(
        selectedTabIndex = selectedTab,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        contentColor = Color.White,
        indicator = {},
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTab == index

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFB400FF).copy(alpha = 0.35f),
                                Color.Transparent
                            ),
                            radius = 300f
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        if (isSelected)
                            Color.Black.copy(alpha = 0.55f)
                        else Color.Black.copy(alpha = 0.30f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null // ← no ripple, no state layer
                    ) {
                        onTabSelected(index)
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = Color.White
                )
            }

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
                            onCancel = {
                                ordersViewModel.cancelOrder(order.id)
                            },
                            userAssetsViewModel = userAssetsViewModel
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
    val integerPart = volume.toLong()
    val integerPartLength = integerPart.toString().length
    val adjustedDecimals = if (integerPartLength >= 3) 1 else 4
    val safeDecimals = adjustedDecimals.coerceIn(0, 8)
    val formattedVolume = "%.${safeDecimals}f".format(volume)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black.copy(alpha = 0.35f))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // PRICE
            Text(
                text = match.price.toString(),
                color = color,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )

            // VOLUME
            Text(
                text = formattedVolume,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )

            // TIME
            Text(
                text = time,
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
