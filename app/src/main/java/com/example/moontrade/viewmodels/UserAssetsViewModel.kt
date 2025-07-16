package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.data.repository.UserRepository
import com.example.moontrade.model.UserAsset
import com.example.moontrade.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAssetsViewModel @Inject constructor(
    private val repository: UserRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _assets = MutableStateFlow<List<UserAsset>>(emptyList())
    val assets: StateFlow<List<UserAsset>> = _assets.asStateFlow()

    fun loadUserAssets() {
        viewModelScope.launch {
            try {
                val mode = session.mode.value
                println("📥 [UserAssetsViewModel] Loading assets with mode=$mode")

                _assets.value = emptyList()

                val result = repository.fetchUserAssets(mode)

                println("✅ [UserAssetsViewModel] Loaded ${result.size} assets")
                result.forEach {
                    println("→ ${it.asset_name}: amount=${it.amount}")
                }

                _assets.value = result
            } catch (e: Exception) {
                println("❌ [UserAssetsViewModel] Failed to load assets: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
