package com.example.moontrade.ui.screens.components.glasskit

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.example.moontrade.ui.theme.extended

@Composable
fun TotalValueCard(
    amountUi: String,
    currency: String = "USDT",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended
    val shape = RoundedCornerShape(22.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

            Text(
                "TOTAL VALUE",
                color = cs.onSurface,
                style = MaterialTheme.typography.labelLarge
            )

            val amountStyle = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontFeatureSettings = "tnum"
            )
            val currencyStyle = MaterialTheme.typography.labelLarge

            BoxWithConstraints {
                val isNarrow = maxWidth < 240.dp

                if (isNarrow) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = amountUi,
                            style = amountStyle,
                            color = ex.positiveText,
                            maxLines = 1
                        )
                        Text(
                            text = currency,
                            style = currencyStyle,
                            color = cs.onSurface
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = amountUi,
                            style = amountStyle,
                            color = ex.positiveText,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = currency,
                            style = currencyStyle,
                            color = cs.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}
