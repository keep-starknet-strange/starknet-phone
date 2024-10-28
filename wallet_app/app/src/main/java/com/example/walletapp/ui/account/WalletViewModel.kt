package com.example.walletapp.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.walletapp.BuildConfig
import com.example.walletapp.utils.StarknetClient
import com.example.walletapp.utils.weiToEther
import com.example.walletapp.utils.toDoubleWithTwoDecimal
import com.swmansion.starknet.account.Account
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.data.types.Uint256
import com.swmansion.starknet.provider.exceptions.RpcRequestFailedException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class WalletViewModel : ViewModel() {
    private val _balance = MutableStateFlow("")
    val balance: StateFlow<String> get() = _balance
    private val starknetClient = StarknetClient(BuildConfig.RPC_URL)

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> get() = _errorMessage

    fun fetchBalance(accountAddress: Felt) {
        viewModelScope.launch {
            try {
                val getBalance = starknetClient.getEthBalance(accountAddress)
                _balance.value = weiToEther(getBalance).toDoubleWithTwoDecimal()
            } catch (e: RpcRequestFailedException) {
                _errorMessage.value = "${e.code}: ${e.message}"
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error"
            }
        }
    }

    fun transferFunds(account: Account, toAddress: Felt, amount: Uint256) {
        viewModelScope.launch {
            try {
                starknetClient.transferFunds(account, toAddress, amount)
            } catch (e: Exception) {
                println(e)
            }
        }

    }
}
