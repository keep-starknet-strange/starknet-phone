package com.example.walletapp.data.coins

import com.example.walletapp.model.CoinData
import com.example.walletapp.model.TokenIdsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

typealias GetTokenPriceResponse = Response<Map<String, Map<String, Double>>>;

interface CoinGeckoApi {
    @Headers(
        "accept: application/json",
        "x-cg-demo-api-key: CG-mRdWfNFoZnKVan4GNdTrhZjL"
    )
    @GET("simple/price")
    suspend fun getTokenPrices(
        @Query("ids") ids: String, // Comma-separated token IDs
        @Query("vs_currencies") vsCurrencies: String // Comma-separated currency codes
    ): GetTokenPriceResponse


    @Headers(
        "accept: application/json",
        "x-cg-demo-api-key: CG-mRdWfNFoZnKVan4GNdTrhZjL"
    )
    @GET("coins/list")
    suspend fun getTokenIds(@Query("include_platform") includePlatform:Boolean):TokenIdsResponse

    @Headers("accept: application/json", "x-cg-demo-api-key: CG-mRdWfNFoZnKVan4GNdTrhZjL")
    @GET("coins/{id}")
    suspend fun getTokenData(@Path("id") id: String,): Response<CoinData>
}