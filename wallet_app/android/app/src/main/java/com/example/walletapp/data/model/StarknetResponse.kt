package com.example.walletapp.data.model

data class StarknetResponse(
    val jsonrpc: String,
    val id: Int,
    val result: List<String>
)

