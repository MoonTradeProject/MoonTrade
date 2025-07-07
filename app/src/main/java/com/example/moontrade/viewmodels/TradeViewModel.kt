package com.example.moontrade.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.data.repository.TradeRepository
import com.example.moontrade.model.PlaceOrderRequest
import com.example.moontrade.model.toWire
import com.example.moontrade.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val repo: TradeRepository,
    internal val session: SessionManager
) : ViewModel() {

    // UI-state
    val amount    = mutableStateOf("")
    val assetName = mutableStateOf("BTCUSDT")
    val lastBid   = mutableStateOf("0")
    val lastAsk   = mutableStateOf("0")

    /**
     * Places an order using current UI state.
     *
     * @param side "buy" or "sell" in lowercase
     * @param execType "limit" or "market" in lowercase
     */
    fun place(side: String, execType: String = "limit") = viewModelScope.launch {
        Log.d("TradeViewModel", "🚀 place() called with side=$side, amount=${amount.value}, execType=$execType")

        val token = session.getValidToken()
        if (token == null) {
            Log.e("TradeViewModel", "❌ No valid token, cannot place order")
            return@launch
        }

        val mode = session.mode.value
        val (modeStr, tid) = mode.toWire()

        val rawPrice = if (side == "buy") lastAsk.value else lastBid.value

        // Format price to 6 decimal places
        val priceFormatted = rawPrice.toBigDecimalOrNull()
            ?.setScale(6, RoundingMode.HALF_UP)
            ?.stripTrailingZeros()
            ?.toPlainString() ?: "0"

        // Format amount to 6 decimal places
        val amountFormatted = amount.value.toBigDecimalOrNull()
            ?.setScale(6, RoundingMode.HALF_UP)
            ?.stripTrailingZeros()
            ?.toPlainString() ?: "0"

        Log.d("TradeViewModel", "📈 Preparing order: asset=${assetName.value}, price=$priceFormatted, amount=$amountFormatted, mode=$modeStr, tid=$tid")

        val req = PlaceOrderRequest(
            mode = modeStr.lowercase(),           // "main" or "tournament"
            asset_name = assetName.value,
            price = priceFormatted,
            amount = amountFormatted,
            exec_type = execType.lowercase(),     // "limit" or "market"
            order_type = side.lowercase()         // "buy" or "sell"
        )

        Log.d("TradeViewModel", "📤 Sending PlaceOrderRequest: $req")

        runCatching { repo.placeOrder(req, "Bearer $token") }
            .onSuccess { resp ->
                Log.i("TradeViewModel", "✅ Order placed successfully: $resp")
                amount.value = "" // clear input on success
                // TODO: show Snackbar/Toast on success if needed
            }
            .onFailure { e ->
                Log.e("TradeViewModel", "💥 Failed to place order: ${e.localizedMessage}", e)
                // TODO: show Snackbar/Toast on error if needed
            }
    }
}
