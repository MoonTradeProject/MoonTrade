package com.example.moontrade.ui.screens.main_screens.order_sub_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.moontrade.data.response.OrderEntry

@Composable
fun OrderRow(
    order: OrderEntry,
    onCancel: (String) -> Unit = {} // optional cancel callback
) {
    val sideColor =
        if (order.order_type.lowercase() == "buy") Color(0xFF4CAF50)
        else Color(0xFFF44336)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // FIRST ROW: SIDE + STATUS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.order_type.uppercase(),
                    fontWeight = FontWeight.Bold,
                    color = sideColor
                )

                Text(
                    text = order.status,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(4.dp))

            // SECOND ROW: pair + mode
            Text(
                text = order.asset_name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Mode: ${order.mode}",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )

            if (order.tournament_id != "00000000-0000-0000-0000-000000000000") {
                Text(
                    text = "Tournament: ${order.tournament_id}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(6.dp))

            // PRICE / AMOUNT
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Price", color = Color.Gray)
                    Text(order.price)
                }
                Column {
                    Text("Amount", color = Color.Gray)
                    Text(order.amount)
                }
            }

            Spacer(Modifier.height(8.dp))

            // CANCEL BUTTON IF PENDING
            if (order.status.lowercase() == "pending") {
                Button(
                    onClick = { onCancel(order.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF5252)
                    )
                ) {
                    Text("Cancel Order")
                }
            }
        }
    }
}
