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
import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.TopBarWithBackButton
import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.TradeForm
import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.TradeMatchesList
import com.example.moontrade.utils.PriceCounter
import com.example.moontrade.viewmodels.BalanceViewModel
import com.example.moontrade.viewmodels.MarketDetailViewModel
import com.example.moontrade.viewmodels.TradeViewModel
import com.example.moontrade.viewmodels.UserAssetsViewModel
import java.math.BigDecimal

@Composable
fun MarketDetailScreen(
    navController: NavController,
    symbol: String,
    viewModel: MarketDetailViewModel,
    userAssetsViewModel: UserAssetsViewModel,
    balanceViewModel: BalanceViewModel
) {
    val tradeViewModel: TradeViewModel = hiltViewModel()
    val snapshot by viewModel.snapshot.collectAsState()
    val userAssets by userAssetsViewModel.assets.collectAsState()
    val balance by balanceViewModel.balance.collectAsState()
    val formattedBalanceString = balance.removeSuffix("USDT").trim()
    ///////related to asset balance item (REFACTOR)///////
    val assetBalance = userAssets
        .firstOrNull { it.asset_name.equals(symbol, ignoreCase = true) }
        ?.amount ?: 0.0
    val decimals: Int = 3
    val assetBalanceDecimal: BigDecimal = assetBalance.toString()
        .toBigDecimalOrNull()
        ?: BigDecimal.ZERO
    val integerPartLength = assetBalance.toInt().toString().length
    val adjustedDecimals = if (integerPartLength >= 3) 1 else decimals
    val safeDecimals = adjustedDecimals.coerceIn(0, 8)
    val formattedAssetBalance = "%.${safeDecimals}f ${symbol.removeSuffix("USDT")}".format(assetBalance)
    ///////related to asset balance item (REFACTOR)///////

    /////////lower item with tabs//////////
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Last Orders", "My Orders")

    /////////////////////////////

    ////////notif//////
    var showNotification by remember { mutableStateOf(false) }
    var isErrorNotification by remember { mutableStateOf(false) }
    var notificationMessage by remember { mutableStateOf("") }
    ////////notif//////

    //////state from trade from//////////
    var orderType by remember { mutableStateOf("Market") }
    var isBuy by remember { mutableStateOf(true) }
    var price = PriceCounter(snapshot, tradeViewModel.amount.value, if (isBuy) "buy" else "sell")
    /////////////////////////////////////

    val onTradeSuccess: (action: String) -> Unit = { action ->
        notificationMessage = "$action of $symbol successful!"
        isErrorNotification = false
        showNotification = true
    }

    val onTradeError: (errorMessage: String) -> Unit = { errorMessage ->
        notificationMessage = errorMessage
        isErrorNotification = true
        showNotification = true
    }

    val executeTradeBlock: (execType: String, side: String) -> Unit = { execType, side ->

        tradeViewModel.updateSnapshot(snapshot)
        userAssetsViewModel.loadUserAssets()

        tradeViewModel.place(
            side = side,
            execType = execType,
            userAssetsViewModel = userAssetsViewModel,
            onSuccess = {
                val action = if (side == "buy") "Buy" else "Sell"
                onTradeSuccess(action)
            },
            onError = onTradeError,
            balance = formattedBalanceString,
            assetBalance = assetBalanceDecimal,
            symbol = symbol
        )
    }

    LaunchedEffect(symbol) {
        viewModel.disconnect()
        viewModel.connect(symbol)
        userAssetsViewModel.loadUserAssets()
        tradeViewModel.assetName.value = symbol
        println(
            userAssets.joinToString("\n") { asset ->
                "asset_name = ${asset.asset_name}, balance = ${asset.amount}"
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.disconnect()
        }
    }

        Scaffold(
            topBar = {
//                TopBarWithBackButton(symbol = symbol, navController = navController, viewModel = viewModel)
                TopBar(title = symbol, showBack = true, onBack = { navController.popBackStack() })
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            //.weight(1f)
                            .wrapContentHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(3f)
                                .wrapContentHeight()
                                .verticalScroll(rememberScrollState())
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
                                onPriceChange = { price = it },
                                userAssetsViewModel = userAssetsViewModel
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .wrapContentHeight()
                        ) {
                            OrderBookLive(snapshot = snapshot)
                        }
                    }

                    TradeMatchesList(
                        matches = snapshot?.matches?.takeLast(5) ?: emptyList(),
                        selectedTab = selectedTab,
                        tabs = tabs,
                        onTabSelected = { selectedTab = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }

            }

            if (showNotification) {

                LaunchedEffect(showNotification) {
                    kotlinx.coroutines.delay(2000)
                    showNotification = false
                }

                CenterNotification(notificationMessage, isError = isErrorNotification)
            }
        }
}
