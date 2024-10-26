package com.example.walletapp.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.walletapp.BuildConfig
import com.example.walletapp.WalletAppApplication
import com.example.walletapp.ui.account.TokenRepository
import com.example.walletapp.ui.account.TokenViewModel
import com.example.walletapp.utils.StarknetClient

class MainActivity : ComponentActivity() {
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
        // Access the database instance
        val database = (application as WalletAppApplication).database
        val repository = TokenRepository(database.tokenDao())
        val tokenViewModel: TokenViewModel = ViewModelProvider(
            this, TokenViewModel.Factory(application,repository)
        ).get(TokenViewModel::class.java)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            WalletApp(tokenViewModel)
        }
    }
}



