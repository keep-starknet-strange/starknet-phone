package com.example.walletapp.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import com.example.walletapp.BuildConfig
import com.example.walletapp.model.Token
import com.example.walletapp.utils.StarknetClient
import com.example.walletapp.utils.weiToEther
import com.example.walletapp.utils.toDoubleWithTwoDecimal
import com.swmansion.starknet.account.Account
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.data.types.Uint256
import com.swmansion.starknet.provider.exceptions.RpcRequestFailedException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext


class WalletViewModel : ViewModel() {
    private val _balances = MutableStateFlow(emptyList<HashMap<String, Double>>())
    val balances: StateFlow<List<HashMap<String, Double>>> get() = _balances
    val tokenIds = mutableStateListOf<String>()

    private val starknetClient = StarknetClient(BuildConfig.RPC_URL)

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> get() = _errorMessage

    fun fetchBalance(accountAddress: Felt, tokens: List<Token>, coinViewModel: CoinViewModel) {
        viewModelScope.launch {
            if (tokens.isNotEmpty()) {
                tokenIds.addAll(tokens.map { it.tokenId })
                coinViewModel.fetchTokenImages(tokenIds)
                try {
                    coinViewModel.getTokenPrices(
                        ids = tokenIds.joinToString(",") { it },
                        vsCurrencies = "usd"
                    )
                    val balanceDeferred: List<Deferred<HashMap<String, Double>>> = tokens.map { token ->
                        async(Dispatchers.IO) {
                            try {
                                val balanceInWei =
                                    starknetClient.getBalance(accountAddress, token.contactAddress)
                                val balanceInEther = weiToEther(balanceInWei).toDoubleWithTwoDecimal()
                                hashMapOf(token.name to balanceInEther)
                            } catch (e: RpcRequestFailedException) {
                                withContext(Dispatchers.Main) {
                                    _errorMessage.value = "${e.code}: ${e.message}"
                                }
                                hashMapOf(token.name to 0.0)
                            }
                        }
                    }
                    // Wait for all balance fetching to complete
                    _balances.value = balanceDeferred.awaitAll()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = e.message.toString()
                    }
                }
            }
        }
    }

    fun transferFunds(account: Account, toAddress: Felt, amount: Uint256) {
        viewModelScope.launch {
            try {
                //TODO Handle the transaction hash if transaction was successfully
                starknetClient.transferFunds(account, toAddress, amount)
            } catch (e: Exception) {
                println(e)
            }
        }

    }
}
