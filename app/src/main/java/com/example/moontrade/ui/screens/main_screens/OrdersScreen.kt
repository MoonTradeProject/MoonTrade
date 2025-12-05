package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.main_screens.order_sub_screen.OrderRow
import com.example.moontrade.viewmodels.OrdersViewModel

//@Composable
//fun OrdersScreen(
//    navController: NavController,
//    viewModel: OrdersViewModel
//) {
//    val orders by viewModel.orders.collectAsState()
//    val isLoading by viewModel.loading.collectAsState()
//    val error by viewModel.error.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopBar(
//                title = "My Orders",
//                showBack = true,
//                onBack = { navController.popBackStack() }
//            )
//        }
//    ) { padding ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//                .padding(padding)
//                .padding(16.dp)
//        ) {
//
//            if (isLoading) {
//                Box(
//                    Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//                return@Column
//            }
//
//            if (error != null) {
//                Text(
//                    text = "Error: $error",
//                    color = Color.Red,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(8.dp)
//            ) {
//                items(orders) { order ->
//                    OrderRow(
//                        order = order,
//                        onCancel = { viewModel.cancelOrder(order.id) }
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun OrdersScreen(
    navController: NavController,
    viewModel: OrdersViewModel
) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    // 1. STATE: Track which filter is active
    var showActiveOnly by remember { mutableStateOf(false) }

    // 2. LOGIC: Filter using your specific case-insensitive logic
    val displayedOrders = remember(orders, showActiveOnly) {
        if (showActiveOnly) {
            orders.filter { order ->
                val s = order.status
                // 👇 YOUR ROBUST LOGIC HERE
                s.equals("partiallyFilled", ignoreCase = true) ||
                        s.equals("pending", ignoreCase = true) ||
                        s.equals("active", ignoreCase = true)
            }
        } else {
            orders // Show everything
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "My Orders",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp)
        ) {

            // 3. UI: Filter Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterButton(
                    text = "All Orders",
                    isSelected = !showActiveOnly,
                    onClick = { showActiveOnly = false },
                    modifier = Modifier.weight(1f)
                )

                FilterButton(
                    text = "Active Orders",
                    isSelected = showActiveOnly,
                    onClick = { showActiveOnly = true },
                    modifier = Modifier.weight(1f)
                )
            }

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            if (error != null) {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // 4. LIST: Use the filtered list here
                items(
                    items = displayedOrders,
                    key = { it.id }
                ) { order ->
                    OrderRow(
                        order = order,
                        onCancel = { viewModel.cancelOrder(order.id) }
                    )
                }
            }
        }
    }
}

// 5. HELPER: A reusable button component to keep the main code clean
@Composable
fun FilterButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            // If selected: Primary Color. If not: Surface Variant (Grayish)
            containerColor = if (isSelected) cs.primary else cs.surfaceVariant,
            // If selected: OnPrimary (White/Black). If not: OnSurfaceVariant (Dark Gray)
            contentColor = if (isSelected) cs.onPrimary else cs.onSurfaceVariant
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null
    ) {
        Text(text = text)
    }
}
