package com.example.walletapp.ui

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.walletapp.ui.onboarding.border.OnboardingScreen
import com.example.walletapp.ui.theme.WalletappTheme
import com.example.walletapp.ui.wallet.WalletScreen
import kotlinx.serialization.Serializable

@Serializable
object Wallet
@Serializable
object Onboarding

@Composable
fun WalletApp() {
    WalletappTheme {

        // TODO: get this information from a data store
        val hasAccount = false

        fun getStart(): Any {
            return if (hasAccount) {
                Wallet
            } else {
                Onboarding
            }
        }

        val navController = rememberNavController()
        NavHost(navController, startDestination = getStart()) {
            composable<Onboarding> {
                OnboardingScreen(onClickCreateNewWallet = { /*TODO*/ }, onClickImportExistingWallet = { /*TODO*/})
            }
           composable<Wallet> {
               WalletScreen()
           }
        }
    }
}