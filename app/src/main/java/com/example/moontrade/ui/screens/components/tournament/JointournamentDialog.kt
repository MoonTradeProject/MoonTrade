package com.example.moontrade.ui.screens.components.tournament
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*


@Composable
fun JoinTournamentDialog(
    tournamentName: String,
    onDismiss: () -> Unit,
    onConfirm: (JoinMethod) -> Unit
) {
    var selectedMethod by remember { mutableStateOf<JoinMethod?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Join $tournamentName") },
        text = {
            Column {
                Text("Choose how to get your entry stack:")
                Spacer(Modifier.height(12.dp))

                JoinMethod.entries.forEach { method ->
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedMethod == method,
                            onClick = { selectedMethod = method }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(method.label)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedMethod?.let { onConfirm(it) }
                },
                enabled = selectedMethod != null
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

enum class JoinMethod(val label: String) {
    TRANSFER("Transfer from balance"),
    WATCH_AD("Watch an ad"),
    BUY("Buy with real money")
}

