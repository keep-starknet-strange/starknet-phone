package com.example.walletapp


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.walletapp.data.repository.CoinRepository

class CoinViewModel : ViewModel() {
    private val repository = CoinRepository()

    private val _prices = mutableStateOf<Map<String, Double>>(emptyMap()) // Token -> USD price
    val prices: State<Map<String, Double>> = _prices

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    fun getTokenPrices(ids: String,
                       vsCurrencies: String) {
        viewModelScope.launch {
            try {
                // Fetch prices for starknet and bitcoin in USD
                val response = repository.getTokenPrices(ids,vsCurrencies)
                if (response.isSuccessful) {
                    response.body()?.let { priceMap ->
                        val parsedPrices = mutableMapOf<String, Double>()
                        priceMap.forEach { (token, currencyMap) ->
                            parsedPrices[token] = currencyMap["usd"] ?: 0.0
                        }
                        _prices.value = parsedPrices
                    } ?: run {
                        _errorMessage.value = "No data available."
                    }
                } else {
                    _errorMessage.value = "Error: ${response.code()} ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.localizedMessage}"
            }
        }
    }
}
