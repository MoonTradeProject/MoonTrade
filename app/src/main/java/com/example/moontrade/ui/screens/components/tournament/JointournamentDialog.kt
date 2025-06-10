package com.example.moontrade.ui.screens.components.tournament

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontrade.data.enums.TournamentPaymentMethod

@Composable
fun JoinTournamentDialog(
    tournamentName: String,
    onDismiss: () -> Unit,
    onConfirm: (TournamentPaymentMethod) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Join $tournamentName") },
        text = {
            Column {
                Text("Choose how you want to join:")
                Spacer(Modifier.height(12.dp))

                Button(onClick = { onConfirm(TournamentPaymentMethod.MainBalance) }) {
                    Text("Pay with Main Balance")
                }
                Spacer(Modifier.height(8.dp))
                Button(onClick = { onConfirm(TournamentPaymentMethod.Ad) }) {
                    Text("Watch Ad (mocked)")
                }
                Spacer(Modifier.height(8.dp))
                Button(onClick = { onConfirm(TournamentPaymentMethod.Purchase) }) {
                    Text("Purchase Entry (mocked)")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
