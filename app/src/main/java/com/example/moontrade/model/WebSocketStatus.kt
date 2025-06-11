package com.example.moontrade.model

sealed class WebSocketStatus {
    data object Idle : WebSocketStatus()
    data object Connecting : WebSocketStatus()
    data object Connected : WebSocketStatus()
    data class Error(val reason: String) : WebSocketStatus()
}
