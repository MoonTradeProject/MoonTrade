import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
    val price = PriceCounter(
        snapshot,
        tradeViewModel.amount.value,
        if (isBuy) "buy" else "sell"
    )

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
                    .verticalScroll(mainScrollState)
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    // LEFT: Trade form
                    Column(
                        modifier = Modifier
                            .weight(3.5f)
//                            .verticalScroll(rememberScrollState())
                            .padding(end = 8.dp)
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
                            onPriceChange = { },
                            navController = navController,
                            userAssetsViewModel = userAssetsViewModel
                        )
                    }

                    // RIGHT: OrderBook
                    Column(
                        modifier = Modifier
                            .weight(2.5f)
                            .wrapContentHeight()
                    ) {
                        OrderBookLive(snapshot = snapshot)
                    }
                }

                // Bottom: matches / active orders
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
//                        .weight(1f)
                        .height(400.dp)
                )
            }

            // Notification
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

