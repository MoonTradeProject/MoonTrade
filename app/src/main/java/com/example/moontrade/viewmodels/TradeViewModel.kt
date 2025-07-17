package com.example.moontrade.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.data.repository.TradeRepository
import com.example.moontrade.model.OrderBookSnapshot
import com.example.moontrade.model.PlaceOrderRequest
import com.example.moontrade.model.toWire
import com.example.moontrade.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val repo: TradeRepository,
    private val session: SessionManager
) : ViewModel() {

    val balance: StateFlow<String> get() = session.balance
    val amount = mutableStateOf("")
    val assetName = mutableStateOf("BTCUSDT")

    private var snapshot: OrderBookSnapshot? = null

    fun updateSnapshot(newSnapshot: OrderBookSnapshot?) {
        snapshot = newSnapshot
    }
    val lastAsk get() = snapshot?.asks?.firstOrNull()?.price?.toString() ?: "-"
    val lastBid get() = snapshot?.bids?.firstOrNull()?.price?.toString() ?: "-"

    fun place(side: String, execType: String = "limit") = viewModelScope.launch {
        Log.d("TradeViewModel", "\uD83D\uDE80 place() called with side=$side, amount=${amount.value}, execType=$execType")

        val token = session.getValidToken()
        if (token == null) {
            Log.e("TradeViewModel", "\u274C No valid token, cannot place order")
            return@launch
        }

        val mode = session.mode.value
        val (modeStr, tid) = mode.toWire()

        val rawPrice = when (side.lowercase()) {
            "buy"  -> snapshot?.asks?.firstOrNull()?.price?.toString()
            "sell" -> snapshot?.bids?.firstOrNull()?.price?.toString()
            else   -> null
        } ?: "0"

        val priceFormatted = rawPrice.toBigDecimalOrNull()
            ?.setScale(6, RoundingMode.HALF_UP)
            ?.stripTrailingZeros()
            ?.toPlainString() ?: "0"

        val amountFormatted = amount.value.toBigDecimalOrNull()
            ?.setScale(6, RoundingMode.HALF_UP)
            ?.stripTrailingZeros()
            ?.toPlainString() ?: "0"

        Log.d(
            "TradeViewModel",
            "\uD83D\uDCC8 Preparing order: asset=${assetName.value}, price=$priceFormatted, amount=$amountFormatted, mode=$modeStr, tid=$tid"
        )

        val req = PlaceOrderRequest(
            mode = mode.toJson(),
            asset_name = assetName.value,
            price = priceFormatted,
            amount = amountFormatted,
            exec_type = execType,
            order_type = side
        )

        Log.d("TradeViewModel", "\uD83D\uDCE4 Sending PlaceOrderRequest: $req")

        runCatching { repo.placeOrder(req, "Bearer $token") }
            .onSuccess { resp ->
                Log.i("TradeViewModel", "\u2705 Order placed successfully: $resp")
                amount.value = ""
            }
            .onFailure { e ->
                Log.e("TradeViewModel", "\uD83D\uDCA5 Failed to place order: ${e.localizedMessage}", e)
            }
    }
}
