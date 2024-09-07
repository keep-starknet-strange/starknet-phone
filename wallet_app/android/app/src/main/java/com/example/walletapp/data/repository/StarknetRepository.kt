package com.example.walletapp.data.repository

import android.util.Log
import com.example.walletapp.data.datasource.RetrofitInstance
import com.example.walletapp.data.model.StarknetResponse
import retrofit2.Call

class StarknetRepository() {
    fun getAccountBalance(
        contractAddress: String,
        accountAddress: String
    ): Call<StarknetResponse> {
        val calldata = listOf(accountAddress)
        val params = listOf(
            mapOf(
                "contract_address" to "0x049d36570d4e46f48e99674bd3fcc84644ddd6b96f7c741b1562b82f9e004dc7",
                "calldata" to listOf("0x02dc260794e4c2eeae87b1403a88385a72c18a5844d220b88117b2965a8cf3a5"),
                "entry_point_selector" to "0x2e4263afad30923c891518314c3c95dbe830a16874e8abc5777a9a20b54c76e"
            ),
            "latest"
        )
        val request = StarknetCallRequest(params = params)

        return RetrofitInstance.api.getBalance(request)
    }
}
