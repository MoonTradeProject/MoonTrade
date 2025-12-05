package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.data.repository.OrdersRepository
import com.example.moontrade.data.response.OrderEntry
import com.example.moontrade.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val repo: OrdersRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderEntry>>(emptyList())
    val orders: StateFlow<List<OrderEntry>> = _orders.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

//    init {
//        viewModelScope.launch {
//            sessionManager.mode.collect {
//                loadOrders()
//            }
//        }
//    }
    init {
        viewModelScope.launch {
            loadOrders()
            sessionManager.mode.collect {
                loadOrders()
            }
        }
    }


    fun loadOrders() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val response = repo.getOrders()
                _orders.value = response.orders

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun cancelOrder(id: String) {
        viewModelScope.launch {
            try {
                repo.cancelOrder(id)
                loadOrders()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
