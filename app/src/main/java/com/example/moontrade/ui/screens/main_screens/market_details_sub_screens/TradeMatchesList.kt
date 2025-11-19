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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moontrade.model.TradeMatch
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
    modifier: Modifier = Modifier
) {

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
            reverseLayout = true
        ) {
            items(matches) { match ->
                TradeMatchRow(match)
            }
        }

        1 -> MyOrders()
    }

}

@Composable
fun TradeMatchRow(match: TradeMatch) {
    val color = if (match.side.lowercase() == "buy") Color(0xFF4CAF50)
    else Color(0xFFF44336)

    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val time = sdf.format(Date(match.timestamp))

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
            text = match.qty.toString(),
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
