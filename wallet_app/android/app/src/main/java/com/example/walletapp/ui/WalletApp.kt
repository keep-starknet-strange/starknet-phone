package com.example.walletapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.walletapp.ui.onboarding.CreateAccountScreen
import com.example.walletapp.ui.onboarding.ImportAccountScreen
import com.example.walletapp.ui.onboarding.border.OnboardingScreen
import com.example.walletapp.ui.theme.WalletappTheme
import com.example.walletapp.ui.wallet.WalletScreen
import kotlinx.serialization.Serializable

@Serializable
object Wallet
@Serializable
object Onboarding
@Serializable
object CreateAccount
@Serializable
object ImportAccount

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
                OnboardingScreen(
                    onNavigateCreateAccount = { navController.navigate( route = CreateAccount ) },
                    onNavigateImportAccount = { navController.navigate( route = ImportAccount ) }
                )
            }
           composable<Wallet> {
               WalletScreen()
           }
            composable<CreateAccount> {
                CreateAccountScreen()
            }
            composable<ImportAccount> {
                ImportAccountScreen()
            }
        }
    }
}