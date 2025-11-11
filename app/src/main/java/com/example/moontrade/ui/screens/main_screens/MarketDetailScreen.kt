package com.example.moontrade.ui.screens.main_screens

//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.OrderBookLive
//import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.TopBarWithBackButton
//import com.example.moontrade.ui.screens.main_screens.market_details_sub_screens.TradeForm
//import com.example.moontrade.viewmodels.MarketDetailViewModel
//import com.example.moontrade.viewmodels.TradeViewModel
//
//@Composable
//fun MarketDetailScreen(
//    navController: NavController,
//    symbol: String,
//    viewModel: MarketDetailViewModel
//) {
//    val tradeViewModel: TradeViewModel = hiltViewModel()
//
//    val snapshot by viewModel.snapshot.collectAsState()
//
//    LaunchedEffect(symbol) {
//        viewModel.connect(symbol)
//        tradeViewModel.assetName.value = symbol
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(16.dp)
//    ) {
//        TopBarWithBackButton(symbol = symbol, navController = navController)
//
//        Spacer(Modifier.height(16.dp))
//
//
//
//        Spacer(Modifier.height(12.dp))
//        Row(Modifier.fillMaxWidth()) {
//            OrderBookLive(snapshot = snapshot)
//        }
//
//
//        Spacer(Modifier.height(24.dp))
//        TradeForm(tradeViewModel = tradeViewModel, snapshot = snapshot)
//
//    }
//}

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
import com.example.moontrade.viewmodels.MarketDetailViewModel
import com.example.moontrade.viewmodels.TradeViewModel

@Composable
fun MarketDetailScreen(
    navController: NavController,
    symbol: String,
    viewModel: MarketDetailViewModel
) {
    val tradeViewModel: TradeViewModel = hiltViewModel()
    val snapshot by viewModel.snapshot.collectAsState()

    LaunchedEffect(symbol) {
        viewModel.connect(symbol)
        tradeViewModel.assetName.value = symbol
    }

    Scaffold(
        topBar = {
            TopBarWithBackButton(symbol = symbol, navController = navController, viewModel = viewModel)
        }
    ) { innerPadding ->

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(end = 8.dp)
            ) {
                TradeForm(tradeViewModel = tradeViewModel, snapshot = snapshot)
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {
                OrderBookLive(snapshot = snapshot)
            }
        }
    }
}

