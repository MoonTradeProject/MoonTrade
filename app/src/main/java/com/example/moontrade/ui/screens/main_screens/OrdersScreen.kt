package com.example.moontrade.ui.screens.main_screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moontrade.data.response.OrderEntry
import com.example.moontrade.ui.screens.main_screens.order_sub_screen.OrderRow
import com.example.moontrade.viewmodels.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: NavController,
    viewModel: OrdersViewModel
) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        TopAppBar(
            title = { Text("My Orders", fontWeight = FontWeight.Bold) }
        )

        if (isLoading) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }

        if (error != null) {
            Text(
                text = "Error: $error",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(orders) { order ->
                OrderRow(
                    order = order,
                    onCancel = { viewModel.cancelOrder(order.id) }
                )
            }
        }
    }
}
