package com.example.walletapp.data.coins

import com.example.walletapp.di.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoinRepository {
    suspend fun getTokenPrices(
        ids: String,
        vsCurrencies: String
    ): GetTokenPriceResponse {
        return withContext(Dispatchers.IO) {
            RetrofitClient.apiService.getTokenPrices(ids, vsCurrencies)
        }
    }
}