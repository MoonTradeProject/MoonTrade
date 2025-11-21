import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.OrderBookLive
import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.TopBarWithBackButton
import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.TradeForm
import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.TradeMatchesList
import com.example.moontrade.viewmodels.MarketDetailViewModel
import com.example.moontrade.viewmodels.TradeViewModel
import com.example.moontrade.viewmodels.UserAssetsViewModel

@Composable
fun MarketDetailScreen(
    navController: NavController,
    symbol: String,
    viewModel: MarketDetailViewModel,
    userAssetsViewModel: UserAssetsViewModel,
) {
    val tradeViewModel: TradeViewModel = hiltViewModel()
    val snapshot by viewModel.snapshot.collectAsState()
    val userAssets by userAssetsViewModel.assets.collectAsState()
    ///////related to asset balance item (REFACTOR)///////
    val assetBalance = userAssets
        .firstOrNull { it.asset_name.equals(symbol, ignoreCase = true) }
        ?.amount ?: 0.0
    val decimals: Int = 3
    val integerPartLength = assetBalance.toInt().toString().length
    val adjustedDecimals = if (integerPartLength >= 3) 1 else decimals
    val safeDecimals = adjustedDecimals.coerceIn(0, 8)
    val formattedAssetBalance = "%.${safeDecimals}f ${symbol.removeSuffix("USDT")}".format(assetBalance)
    ///////related to asset balance item (REFACTOR)///////

    /////////lower item with tabs//////////
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All Orders", "My Orders")

    /////////////////////////////

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
            TopBarWithBackButton(symbol = symbol, navController = navController, viewModel = viewModel)
        }
    ) { innerPadding ->

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
                    TradeForm(tradeViewModel = tradeViewModel, snapshot = snapshot, assetBalance = formattedAssetBalance, userAssetsViewModel = userAssetsViewModel )
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
                matches = snapshot?.matches?.takeLast(15) ?: emptyList(),
                selectedTab = selectedTab,
                tabs = tabs,
                onTabSelected = { selectedTab = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
