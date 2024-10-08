package com.example.walletapp.data.datasource

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CoinGeckoApi {
    @Headers(
        "accept: application/json",
        "x-cg-demo-api-key: CG-mRdWfNFoZnKVan4GNdTrhZjL"
    )
    @GET("simple/price")
    suspend fun getTokenPrices(
        @Query("ids") ids: String, // Comma-separated token IDs
        @Query("vs_currencies") vsCurrencies: String // Comma-separated currency codes
    ): Response<Map<String, Map<String, Double>>>
}
