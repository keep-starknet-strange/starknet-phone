package com.example.walletapp.data.repository

import com.example.walletapp.di.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CoinRepository {
    suspend fun getTokenPrices(
        ids: String,
        vsCurrencies: String
    ): Response<Map<String, Map<String, Double>>> {
        return withContext(Dispatchers.IO) {
            RetrofitClient.apiService.getTokenPrices(ids, vsCurrencies)
        }
    }
}