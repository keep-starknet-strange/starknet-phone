package com.example.walletapp.ui.account


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.walletapp.data.coins.CoinRepository
import com.example.walletapp.model.CoinData
import com.example.walletapp.model.TokenIdsResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class CoinViewModel : ViewModel() {
    private val repository = CoinRepository()

    private val _prices = mutableStateOf<Map<String, Double>>(emptyMap()) // Token -> USD price
    val prices: State<Map<String, Double>> = _prices

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    private val _tokenIds = mutableStateOf(TokenIdsResponse()) // Token ID list
    val tokenIds: State<TokenIdsResponse> = _tokenIds

    private val _coinData = mutableStateOf<CoinData?>(null)
    val coinData: State<CoinData?> = _coinData

    // State to hold the images map
    private val _tokenImages = mutableStateOf<HashMap<String, String>>(hashMapOf())
    val tokenImages: State<HashMap<String, String>> = _tokenImages

    fun getTokenPrices(ids: String, vsCurrencies: String) {
        viewModelScope.launch {
            try {
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

    // Function to fetch token IDs
    fun getTokenIds() {
        viewModelScope.launch {
            try {
                val response = repository.getTokenIds()
                _tokenIds.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.localizedMessage}"
            }
        }
    }

    //function to fetch token data
    fun getTokenData(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTokenData(id)
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        _coinData.value = data
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
    // Function to fetch token images in parallel
    fun fetchTokenImages(tokenIds: List<String>) {
        viewModelScope.launch {
            try {
                // Create async requests for each tokenId
                val requests = tokenIds.map { tokenId ->
                    async {
                        val response = repository.getTokenData(tokenId)
                        if (response.isSuccessful) {
                            response.body()?.let { coinData ->
                                // Return tokenId and its image URL if available
                                tokenId to coinData.image?.large
                            }
                        } else {
                            null // If not successful, return null
                        }
                    }
                }

                // Wait for all requests to complete and filter out null results
                val results = requests.awaitAll().filterNotNull()

                // Populate the HashMap with tokenId and image URL
                val imageMap = hashMapOf<String, String>()
                results.forEach { (tokenId, imageUrl) ->
                    if (imageUrl != null) {
                        imageMap[tokenId] = imageUrl
                    }
                }

                // Update the state with the new HashMap
                _tokenImages.value = imageMap
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.localizedMessage}"
            }
        }
    }
}
