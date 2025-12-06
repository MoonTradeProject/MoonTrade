
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.CenterNotification
import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.OrderBookLive
import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.TradeForm
import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.TradeMatchesList
import com.example.moontrade.utils.PriceCounter
import com.example.moontrade.viewmodels.BalanceViewModel
import com.example.moontrade.viewmodels.MarketDetailViewModel
import com.example.moontrade.viewmodels.OrdersViewModel
import com.example.moontrade.viewmodels.TradeViewModel
import com.example.moontrade.viewmodels.UserAssetsViewModel
import java.math.BigDecimal

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MarketDetailScreen(
    navController: NavController,
    symbol: String,
    viewModel: MarketDetailViewModel,
    userAssetsViewModel: UserAssetsViewModel,
    balanceViewModel: BalanceViewModel,
    tradeViewModel: TradeViewModel,
    ordersViewModel: OrdersViewModel
) {
    val mainScrollState = rememberScrollState()

    val snapshot by viewModel.snapshot.collectAsState()
    val balance by balanceViewModel.balance.collectAsState()
    val userAssets by userAssetsViewModel.assets.collectAsState()

    // ---------- Local UI state ----------
    var orderType by remember { mutableStateOf("Market") }
    var isBuy by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(0) }

    // ---------- LIFECYCLE: connect / disconnect orderbook ----------
    LaunchedEffect(symbol) {
        viewModel.connect(symbol)
        tradeViewModel.assetName.value = symbol
        userAssetsViewModel.loadUserAssets()
    }

    DisposableEffect(symbol) {
        onDispose {
            viewModel.disconnect()
        }
    }

    // ---------- Balance / assets formatting ----------
    val formattedBalanceString = balance.removeSuffix("USDT").trim()

    val assetBalance = userAssets
        .firstOrNull { it.asset_name.equals(symbol, ignoreCase = true) }
        ?.available ?: 0.0

    val decimals = 3
    val assetBalanceDecimal: BigDecimal =
        assetBalance.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO

    val integerPartLength = assetBalance.toInt().toString().length
    val adjustedDecimals = if (integerPartLength >= 3) 1 else decimals
    val safeDecimals = adjustedDecimals.coerceIn(0, 8)

    val formattedAssetBalance =
        "%.${safeDecimals}f ${symbol.removeSuffix("USDT")}".format(assetBalance)

    // ---------- Price Counter ----------
    val rawPrice = PriceCounter(
        snapshot,
        tradeViewModel.amount.value,
        if (isBuy) "buy" else "sell"
    )

// Нормализуем к Double, без жёсткого каста
    val price: Double = when (rawPrice) {
        is Number -> rawPrice.toDouble()
        is String -> rawPrice.toDoubleOrNull() ?: 0.0
        else -> 0.0
    }


    // ---------- Notifications ----------
    var showNotification by remember { mutableStateOf(false) }
    var notificationMessage by remember { mutableStateOf("") }
    var isErrorNotification by remember { mutableStateOf(false) }
    var isLoadingNotification by remember { mutableStateOf(false) }

    fun notifyLoading() {
        notificationMessage = "Loading..."
        isErrorNotification = false
        isLoadingNotification = true
        showNotification = true
    }

    fun notifySuccess(action: String) {
        notificationMessage = "$action of $symbol successful!"
        isErrorNotification = false
        showNotification = true
    }

    fun notifyError(msg: String) {
        notificationMessage = msg
        isErrorNotification = true
        showNotification = true
    }

    // ---------- Trade execution ----------
    val executeTradeBlock: (String, String) -> Unit = { execType, side ->
        tradeViewModel.updateSnapshot(snapshot)
        userAssetsViewModel.loadUserAssets()

        tradeViewModel.place(
            side = side,
            execType = execType,
            userAssetsViewModel = userAssetsViewModel,
            onSuccess = {
                notifySuccess(if (side == "buy") "Buy" else "Sell")
            },
            onError = ::notifyError,
            balance = formattedBalanceString,
            assetBalance = assetBalanceDecimal,
            ordersViewModel = ordersViewModel,
            symbol = symbol
        )
    }

    val horizontalPadding = 8.dp
    val verticalPadding = 8.dp

    // ---------- UI ----------
    Scaffold(
        topBar = {
            TopBar(
                title = symbol,
                showBack = true,
                onBack = {
                    viewModel.disconnect()
                    navController.popBackStack()
                }
            )
        }
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(
                        start = horizontalPadding,
                        end = horizontalPadding,
                        top = verticalPadding,
                        bottom = verticalPadding
                    )
                    .verticalScroll(mainScrollState)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(3f)       // было 3.5
                            .padding(end = 6.dp)
                    ) {
                        TradeForm(
                            tradeViewModel = tradeViewModel,
                            snapshot = snapshot,
                            assetBalance = formattedAssetBalance,
                            onExecuteTrade = executeTradeBlock,
                            orderType = orderType,
                            onOrderTypeChange = { orderType = it },
                            isBuy = isBuy,
                            onBuySellChange = { isBuy = it },
                            price = price,
                            navController = navController,
                            userAssetsViewModel = userAssetsViewModel
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(2.2f)     // было 2.5
                    ) {
                        OrderBookLive(snapshot = snapshot)
                    }
                }


                TradeMatchesList(
                    matches = snapshot?.matches?.take(10) ?: emptyList(),
                    selectedTab = selectedTab,
                    tabs = listOf("Last Orders", "Active Orders"),
                    onTabSelected = { selectedTab = it },
                    navController = navController,
                    ordersViewModel = ordersViewModel,
                    userAssetsViewModel = userAssetsViewModel,
                    symbol = symbol,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
            }

            if (showNotification) {
                LaunchedEffect(showNotification) {
                    kotlinx.coroutines.delay(2000)
                    showNotification = false
                }

                CenterNotification(
                    notificationMessage,
                    isErrorNotification
                )
            }
        }
    }
}
