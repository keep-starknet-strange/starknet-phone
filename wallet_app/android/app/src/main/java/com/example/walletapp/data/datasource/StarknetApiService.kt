package com.example.walletapp.data.datasource

import com.example.walletapp.data.model.StarknetResponse
import com.example.walletapp.data.repository.StarknetCallRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface StarknetApiService {


    @POST("rFAP8fkTAz9TmYw8_V5Fyzxi-WSoQdhk") // rpc end point
    fun getBalance( @Body request: StarknetCallRequest): Call<StarknetResponse>
}