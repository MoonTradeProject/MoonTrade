package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.data.repository.AssetsRepository
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
    private val repository: AssetsRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _assets = MutableStateFlow<List<UserAsset>>(emptyList())
    val assets: StateFlow<List<UserAsset>> = _assets.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadUserAssets() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val mode = session.mode.value
                println("📥 [UserAssetsViewModel] Loading assets with mode=$mode")

                val result = repository.fetchUserAssets(mode)

                println("✅ [UserAssetsViewModel] Loaded ${result.size} assets")
                _assets.value = result
            } catch (e: Exception) {
                println("❌ [UserAssetsViewModel] Failed to load assets: ${e.message}")
                _error.value = e.message ?: "Failed to load assets"
            } finally {
                _loading.value = false
            }
        }
    }
}
