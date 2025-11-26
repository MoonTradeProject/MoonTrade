package com.example.moontrade.ui.screens.main_screens.order_sub_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.moontrade.data.response.OrderEntry
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.theme.GreenUp
import com.example.moontrade.ui.theme.RedDown
import com.example.moontrade.ui.theme.Violet300
import com.example.moontrade.ui.theme.Violet600

@Composable
fun OrderRow(
    order: OrderEntry,
    modifier: Modifier = Modifier,
    onCancel: (String) -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme

    val isBuy = order.order_type.equals("buy", ignoreCase = true)
    val sideColor = if (isBuy) GreenUp else RedDown

    // parse amounts safely
    val original = order.original_amount.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO
    val executed = order.executed_amount.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO
    val remaining = order.amount.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO
    val progress =
        if (original > java.math.BigDecimal.ZERO) executed.divide(original, 4, java.math.RoundingMode.HALF_UP)
            .toFloat()
        else 0f

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            // --- HEADER ROW: BUY/SELL + STATUS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.order_type.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = sideColor
                )

                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Violet600.copy(alpha = 0.9f),
                                    Violet300.copy(alpha = 0.9f)
                                )
                            ),
                            shape = RoundedCornerShape(40.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = order.status.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            // --- ASSET & MODE ---
            Text(
                text = order.asset_name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = cs.onSurface
            )
            Text(
                text = "Mode: ${order.mode}",
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurfaceVariant
            )
            if (order.tournament_id != "00000000-0000-0000-0000-000000000000") {
                Text(
                    text = "Tournament: ${order.tournament_id}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(10.dp))


            // --- PRICE & FILLS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Price",
                        style = MaterialTheme.typography.labelMedium,
                        color = cs.onSurfaceVariant
                    )
                    Text(
                        text = order.price,
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Executed / Total",
                        style = MaterialTheme.typography.labelMedium,
                        color = cs.onSurfaceVariant
                    )
                    Text(
                        text = "${order.executed_amount} / ${order.original_amount}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurface
                    )

                    Text(
                        text = "Remaining: ${order.amount}",
                        style = MaterialTheme.typography.labelSmall,
                        color = cs.onSurfaceVariant
                    )
                }
            }

            // --- PROGRESS BAR ---
            Spacer(Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = sideColor,
                trackColor = cs.surfaceVariant
            )

            // --- CANCEL BUTTON ---
            if (order.status.equals("pending", ignoreCase = true)) {
                Spacer(Modifier.height(10.dp))
                TextButton(
                    onClick = { onCancel(order.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel order")
                }
            }
        }
    }
}
