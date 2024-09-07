package com.example.walletapp.data.datasource

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Replace this with the actual StarkNet RPC URL
    private const val BASE_URL = "https://starknet-mainnet.g.alchemy.com/starknet/version/rpc/v0_7/"

    val api: StarknetApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StarknetApiService::class.java)
    }
}
