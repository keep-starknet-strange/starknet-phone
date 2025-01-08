package com.snphone.snwalletsdk

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.snphone.snwalletsdk.utils.StarknetClient
import com.swmansion.starknet.account.StandardAccount
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.data.types.StarknetChainId
import com.swmansion.starknet.extensions.toFelt
import com.swmansion.starknet.provider.rpc.JsonRpcProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        instance = this
        setContentView(R.layout.activity_main)

        val starknetClient = StarknetClient(BuildConfig.RPC_URL)
        lifecycleScope.launch(Dispatchers.IO) {

            //Deploy account
            //val (privateKey, accountAddress) = deployAccount()

            //GetBalance
            //val balance = getBalance(accountAddress)
            val addressUser = BuildConfig.publicAddress.toFelt
            val privateKey = BuildConfig.privateKey.toFelt

            val account = StandardAccount(
                address = addressUser,
                privateKey = privateKey,
                provider = JsonRpcProvider(BuildConfig.RPC_URL),
                chainId = StarknetChainId.SEPOLIA,
            )
            val recipientAddress = Felt.fromHex(BuildConfig.recepientAddress)
            try {
                val amount = StarknetClient.toUint256Amount(1.toString())
                val address = starknetClient.transferFunds(account,recipientAddress,amount)
                Log.d("MainActivity", "transaction id: $address")

            }catch (e: Exception){
                Log.d("MainActivity", "Error in amount: $e")

            }
        }
    }
}