package com.example.moontrade.ui.screens.main_screens.order_sub_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import com.example.moontrade.data.response.OrderEntry
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.theme.GreenUp
import com.example.moontrade.ui.theme.RedDown
import com.example.moontrade.ui.theme.Violet300
import com.example.moontrade.ui.theme.Violet600
import com.example.moontrade.viewmodels.UserAssetsViewModel

@Composable
fun OrderRow(
    order: OrderEntry,
    modifier: Modifier = Modifier,
    onCancel: (String) -> Unit = {},
    userAssetsViewModel: UserAssetsViewModel? = null
) {
    val cs = MaterialTheme.colorScheme

    val isBuy = order.order_type.equals("buy", ignoreCase = true)
    val sideColor = if (isBuy) GreenUp else RedDown
    var showConfirmationDialog by remember { mutableStateOf(false) }
    // parse amounts safely
    val original = order.original_amount.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO
    val executed = order.executed_amount.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO
    val remaining = order.amount.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO
    val progress =
        if (original > java.math.BigDecimal.ZERO) executed.divide(original, 4, java.math.RoundingMode.HALF_UP)
            .toFloat()
        else 0f

    val executedAmountDouble = order.executed_amount.toDoubleOrNull() ?: 0.0

// 2. Safely get the integer part of the amount (e.g., 12.345 -> 12)
    val integerPart = executedAmountDouble.toLong()

// 3. Get the length of the integer part string representation
    val integerPartLength = integerPart.toString().length

// The rest of your logic is now safe
    val adjustedDecimals = if (integerPartLength >= 3) 1 else 4
    val safeDecimals = adjustedDecimals.coerceIn(0, 8)
    val formattedPrice = "%.${safeDecimals}f".format(executedAmountDouble)
    val fontSize = when {
        formattedPrice.length > 12 -> 12.sp
        formattedPrice.length > 8 -> 14.sp
        else -> 16.sp
    }

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
                        text = "$formattedPrice / ${order.original_amount}",
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
            if (order.status.equals("active", ignoreCase = true)) {
                Spacer(Modifier.height(10.dp))
//                TextButton(
//                    onClick = {
//                        onCancel(order.id)
//                              },
//                    modifier = Modifier.fillMaxWidth()
//                        .background(
//                        color = Color.Transparent, // Use a background color if desired, e.g., Color.LightGray
//                        shape = RoundedCornerShape(20.dp) // Adjust '20.dp' for desired roundness
//                    )
//                        // 2. Add a border/outline if you want just a line around the text
//                        .border(
//                            width = 1.dp,
//                            color = RedDown, // Use your desired line color (RedDown)
//                            shape = RoundedCornerShape(20.dp) // Ensure the border has the same shape
//                        )
//                ) {
//                    Text(
//                        "Cancel order",
//                        color = RedDown
//                    )
//                }
                TextButton(
                    onClick = {
                        // Set the state to true to show the confirmation dialog
                        showConfirmationDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        // Using a background is often required for the border to appear correctly
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        // Add the rounded line border
                        .border(
                            width = 1.dp,
                            color = RedDown,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(
                        "Cancel order",
                        color = RedDown
                    )
                }

                // --- 2. The Confirmation Dialog ---
                if (showConfirmationDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            // Dismiss the dialog if user clicks outside
                            showConfirmationDialog = false
                        },
                        title = {
                            Text(text = "Confirm Cancellation")
                        },
                        text = {
                            Text(text = "Are you sure you want to cancel the order #${order.id}?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    // Execute the cancellation logic passed from the ViewModel
                                    onCancel(order.id)
                                    userAssetsViewModel?.loadUserAssets()
                                    // Hide the dialog
                                    showConfirmationDialog = false
                                }
                            ) {
                                Text("Yes, Cancel")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    // Hide the dialog without canceling
                                    showConfirmationDialog = false
                                }
                            ) {
                                Text("No")
                            }
                        }
                    )
                }
            }
        }
    }
}
