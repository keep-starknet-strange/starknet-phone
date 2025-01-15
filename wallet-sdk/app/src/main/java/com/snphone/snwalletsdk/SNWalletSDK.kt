package com.snphone.snwalletsdk

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.snphone.snwalletsdk.utils.StarknetClient
import kotlinx.coroutines.launch

class SNWalletSDK : ComponentActivity() {
    init {
        instance = this
    }

    companion object {
        private var instance: SNWalletSDK? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        val starknetClient = StarknetClient(BuildConfig.RPC_URL)
        lifecycleScope.launch {
            starknetClient.deployAccount()
        }
    }
}