package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moontrade.model.TradeMatch
import com.example.moontrade.ui.screens.main_screens.order_sub_screen.OrderRow
import com.example.moontrade.viewmodels.OrdersViewModel
import com.example.moontrade.viewmodels.UserAssetsViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

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
            val isStatusActive =
                order.status.equals("partiallyFilled", ignoreCase = true) ||
                        order.status.equals("pending", ignoreCase = true) ||
                        order.status.equals("active", ignoreCase = true) ||
                        order.exec_type.equals("limit", ignoreCase = true)

            val matchesSymbol = order.asset_name.equals(symbol, ignoreCase = true)
            isStatusActive && matchesSymbol
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Неоновые табы в стиле остального UI
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            containerColor = Color.Transparent,
            contentColor = Color.White,
            indicator = {},
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                val shape = RoundedCornerShape(14.dp)

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 6.dp)
                        .clip(shape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF8A2CFF).copy(alpha = 0.35f),
                                    Color.Transparent
                                ),
                                radius = 260f
                            ),
                            shape = shape
                        )
                        .background(
                            if (isSelected)
                                Color(0xFF05040E)
                            else
                                Color(0xFF05040E).copy(alpha = 0.65f),
                            shape = shape
                        )
                        .clickable {
                            onTabSelected(index)
                        }
                        .padding(horizontal = 18.dp, vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) Color(0xFFEDEEFF) else Color(0xFF9698B6)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        when (selectedTab) {
            // Last Orders (matches)
            0 -> LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                reverseLayout = false
            ) {
                items(matches) { match ->
                    TradeMatchRow(match)
                }
            }

            // Active Orders
            1 -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp, vertical = 4.dp)
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
}

@Composable
fun TradeMatchRow(match: TradeMatch) {
    val isBuy = match.side.lowercase() == "buy"

    val priceColor = if (isBuy) {
        Color(0xFF27E38A) // тот же зелёный, что и в ордербуке
    } else {
        Color(0xFFFF5A6C) // тот же красный, что и в ордербуке
    }

    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val time = sdf.format(Date(match.timestamp))

    val volume = match.qty
    val integerPart = volume.toLong()
    val integerPartLength = integerPart.toString().length
    val adjustedDecimals = if (integerPartLength >= 3) 1 else 4
    val safeDecimals = adjustedDecimals.coerceIn(0, 8)
    val formattedVolume = "%.${safeDecimals}f".format(volume)

    val numberTextStyle = TextStyle(
        fontSize = 13.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.25.sp,
        fontFeatureSettings = "tnum" // табличные цифры, чтобы столбцы не прыгали
    )

    val cardShape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clip(cardShape)
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (isBuy) {
                        listOf(
                            Color(0x3327E38A),
                            Color(0x00000000)
                        )
                    } else {
                        listOf(
                            Color(0x33FF5A6C),
                            Color(0x00000000)
                        )
                    }
                ),
                shape = cardShape
            )
            .background(Color(0xFF05040B).copy(alpha = 0.92f), cardShape)
            .padding(horizontal = 12.dp, vertical = 9.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp), // ← двигает весь контент чуть внутрь
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

        // PRICE
            Text(
                text = "%.4f".format(match.price),
                color = priceColor,
                style = numberTextStyle.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f)
            )

            // VOLUME
            Text(
                text = formattedVolume,
                color = Color(0xFFEDEDF8),
                style = numberTextStyle,
                modifier = Modifier.weight(1f)
            )

            // TIME
            Text(
                text = time,
                color = Color(0xFF8587A2),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    letterSpacing = 0.2.sp
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
