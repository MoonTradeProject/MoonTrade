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

    LaunchedEffect(symbol) {
        viewModel.disconnect()
        viewModel.connect(symbol)
        tradeViewModel.assetName.value = symbol
        println("userAssets = $userAssets")
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
//                        .fillMaxHeight()
                        .wrapContentHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(end = 8.dp)
                ) {
                    TradeForm(tradeViewModel = tradeViewModel, snapshot = snapshot)
                }

                Column(
                    modifier = Modifier
                        .weight(2f)
//                        .fillMaxHeight()
                        .wrapContentHeight()
                ) {
                    OrderBookLive(snapshot = snapshot)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            TradeMatchesList(
                matches = snapshot?.matches?.takeLast(15) ?: emptyList(),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
