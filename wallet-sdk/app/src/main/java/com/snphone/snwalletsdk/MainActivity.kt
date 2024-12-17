package com.snphone.snwalletsdk

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.snphone.snwalletsdk.SNWalletSDK.Companion
import com.snphone.snwalletsdk.utils.StarknetClient
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
            starknetClient.deployAccount()
        }
    }
}