package com.example.walletapp.ui.activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walletapp.data.model.StarknetResponse
import com.example.walletapp.data.repository.StarknetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StarknetViewModel(private val repository: StarknetRepository) : ViewModel() {

    val balanceLiveData = MutableLiveData<String>()
    val errorLiveData = MutableLiveData<String>()

    // Function to trigger API call to fetch balance
    fun fetchAccountBalance(contractAddress: String, accountAddress: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAccountBalance(contractAddress, accountAddress)
                .enqueue(object : Callback<StarknetResponse> {
                    override fun onResponse(
                        call: Call<StarknetResponse>,
                        response: Response<StarknetResponse>
                    ) {
                        if (response.isSuccessful) {
                            balanceLiveData.postValue(response.body()?.result?.get(0) ?: "0")

                        } else {
                            errorLiveData.postValue("Error fetching balance")
                        }
                    }

                    override fun onFailure(call: Call<StarknetResponse>, t: Throwable) {
                        errorLiveData.postValue(t.message)
                    }
                })
        }
    }
}

