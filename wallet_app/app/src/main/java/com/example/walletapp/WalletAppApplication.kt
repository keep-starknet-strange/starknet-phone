package com.example.walletapp

import android.app.Application
import com.example.walletapp.db.TokenDatabase


class WalletAppApplication : Application() {

    // Lazy initialization of the database
    val database: TokenDatabase by lazy { TokenDatabase.getDatabase(this) }
}