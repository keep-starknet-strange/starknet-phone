package com.example.walletapp.data.repository

data class StarknetCallRequest(
    val id: Int = 1,
    val jsonrpc: String = "2.0",
    val method: String = "starknet_call",
    val params: List<Any>
)