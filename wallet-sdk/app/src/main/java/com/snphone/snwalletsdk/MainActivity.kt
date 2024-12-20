package com.snphone.snwalletsdk

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.snphone.snwalletsdk.utils.StarknetClient
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.extensions.toFelt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val ACCOUNT_CLASS_HASH = "0x061dac032f228abef9c6626f995015233097ae253a7f72d68552db02f2971b8f"


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
            val addressUser = "0x0607842ad50f3c1fc467655ed6edecdbfbe5705a9dbbec80b06143d56d4fe8e9".toFelt
            val address = starknetClient.transferFunds(addressUser)
            Log.d("MainActivity", "Balance: $address")
        }
    }
}