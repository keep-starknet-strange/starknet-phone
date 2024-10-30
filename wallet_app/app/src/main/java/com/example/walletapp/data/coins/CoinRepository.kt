

package com.example.walletapp.data.coins

import com.example.walletapp.di.RetrofitClient
import com.example.walletapp.model.CoinData
import com.example.walletapp.model.TokenIdsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CoinRepository {

    suspend fun getTokenPrices(
        ids: String,
        vsCurrencies: String
    ): GetTokenPriceResponse {
        return withContext(Dispatchers.IO) {
            RetrofitClient.apiService.getTokenPrices(ids, vsCurrencies)
        }
    }

    suspend fun getTokenIds(
    ): TokenIdsResponse {
        return withContext(Dispatchers.IO) {
            RetrofitClient.apiService.getTokenIds(false)
        }
    }

    suspend fun getTokenData(id:String):Response<CoinData>{
        return withContext(Dispatchers.IO) {
            RetrofitClient.apiService.getTokenData(id)
        }
    }
}
