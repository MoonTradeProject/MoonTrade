package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.data.repository.MarketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val repo: MarketRepository
) : ViewModel() {

    private val _markets = MutableStateFlow<List<String>>(emptyList())
    val markets: StateFlow<List<String>> = _markets.asStateFlow()

    init {
        loadMarkets()
    }

    private fun loadMarkets() {
        viewModelScope.launch {
            val result = repo.getMarkets()
            if (result.isSuccess) {
                _markets.value = result.getOrThrow()
            } else {
                println("[markets] failed: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}
