package com.example.moontrade.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
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
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val repo: TradeRepository,
    private val session: SessionManager
) : ViewModel() {
    /** ---------- PUBLIC STATE BOUND TO THE UI ---------- */
    //val balance: StateFlow<String> get() = session.balance
    val amount    = mutableStateOf("")          // amount typed by the user
    val price     = mutableStateOf("")          // price typed by the user (for limit orders)
    val assetName = mutableStateOf("BTCUSDT")   // current instrument

    /** ---------- BOOK SNAPSHOT FROM WEBSOCKET ---------- */
    private var snapshot: OrderBookSnapshot? = null
    fun updateSnapshot(newSnapshot: OrderBookSnapshot?) { snapshot = newSnapshot }

    val lastAsk get() = snapshot?.asks?.firstOrNull()?.price?.toString() ?: "-"
    val lastBid get() = snapshot?.bids?.firstOrNull()?.price?.toString() ?: "-"

    /**
     * Place an order.
     *
     * @param side      "buy" or "sell"
     * @param execType  "market" or "limit"
     */
    fun place(
        side: String,
        execType: String = "limit",
        userAssetsViewModel: UserAssetsViewModel? = null,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        balance: String,
        assetBalance: BigDecimal,
        symbol: String
    ) = viewModelScope.launch {
        Log.d(
            "TradeViewModel",
            "🚀 place() side=$side execType=$execType amount=${amount.value} price=${price.value}"
        )

        /* ---------- 0. AUTH ---------- */
        val token = session.getValidToken() ?: run {
            Log.e("TradeViewModel", "❌ No valid token, cannot place order")
            return@launch
        }

        /* ---------- 1. RESOLVE PRICE ---------- */
        val rawPrice = when (execType.lowercase()) {
            "limit"  -> price.value                       // user-typed price
            "market" -> when (side.lowercase()) {         // best price from book
                "buy"  -> snapshot?.asks?.firstOrNull()?.price?.toString()
                "sell" -> snapshot?.bids?.firstOrNull()?.price?.toString()
                else   -> null
            } ?: "0"
            else -> "0"
        }

        val priceFormatted = rawPrice.toBigDecimalOrNull()
            ?.setScale(6, RoundingMode.HALF_UP)
            ?.stripTrailingZeros()
            ?.toPlainString() ?: "0"

        val amountFormatted = amount.value.toBigDecimalOrNull()
            ?.setScale(6, RoundingMode.HALF_UP)
            ?.stripTrailingZeros()
            ?.toPlainString() ?: "0"

        /////////////////////////////////////////
        // Convert formatted strings to BigDecimal for calculation
        val priceDecimal = priceFormatted.toBigDecimalOrNull()
            ?: return@launch onError("Invalid price format.")
        val amountDecimal = amountFormatted.toBigDecimalOrNull()
            ?: return@launch onError("Invalid amount format.")
        val formattedBalance = balance.toBigDecimalOrNull()
            ?: return@launch onError("Invalid balance format BALANCE::::$balance")

        /* ---------- 2. BALANCE CHECK (SIMPLIFIED) ---------- */

        when(side.lowercase()) {
            "buy" -> {
                val toPay = priceDecimal * amountDecimal
                if(toPay > formattedBalance){
                    return@launch onError("Balance is too low")
                }
            }
            "sell" -> {
                if(assetBalance < amountDecimal){
                    return@launch onError("Balance of $symbol is too low")
                }
            }
            else -> {
                return@launch onError("Internal error. Invalid trade side specified.")
            }
        }

        /* ---------- 2. BUILD REQUEST ---------- */
        val mode = session.mode.value
        val (modeStr, tid) = mode.toWire()

        Log.d(
            "TradeViewModel",
            "📈 Prepared order asset=${assetName.value}, price=$priceFormatted, amount=$amountFormatted, mode=$modeStr, tid=$tid"
        )

        val req = PlaceOrderRequest(
            mode        = mode.toJson(),
            asset_name  = assetName.value,
            price       = priceFormatted,
            amount      = amountFormatted,
            exec_type   = execType.lowercase(),   // backend expects lower-case
            order_type  = side.lowercase()        // "buy" | "sell"
        )

        Log.d("TradeViewModel", "📤 Sending PlaceOrderRequest: $req")

        /* ---------- 3. SEND ---------- */
        runCatching { repo.placeOrder(req, "Bearer $token") }
            .onSuccess { resp ->
                Log.i("TradeViewModel", "✅ Order placed: $resp")
                amount.value = ""
                if (execType.equals("limit", true)) price.value = "" // clear limit price
                userAssetsViewModel?.loadUserAssets()
                onSuccess()
            }
            .onFailure { e ->
                Log.e("TradeViewModel", "💥 Failed to place order: ${e.localizedMessage}", e)
                onError(e.localizedMessage ?: "Unknown error occurred")
            }


    }
}
