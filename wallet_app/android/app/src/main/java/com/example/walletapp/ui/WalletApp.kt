package com.example.walletapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.walletapp.ui.account.AddTokenScreen
import com.example.walletapp.ui.account.WalletScreen
import com.example.walletapp.ui.onboarding.CreateAccountScreen
import com.example.walletapp.ui.onboarding.CreatePinScreen
import com.example.walletapp.ui.onboarding.ImportAccountScreen
import com.example.walletapp.ui.onboarding.border.OnboardingScreen
import com.example.walletapp.ui.theme.WalletappTheme
import com.example.walletapp.ui.transfer.ReceiveScreen
import com.example.walletapp.ui.transfer.SendScreen
import kotlinx.serialization.Serializable

// main user account view
@Serializable
object Wallet
@Serializable
object AddToken

// onboarding flow
@Serializable
object Onboarding
@Serializable
object CreateAccount
@Serializable
object ImportAccount
@Serializable
object CreatePin

// token transfer flow
@Serializable
object Send
@Serializable
object Receive

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
            composable<CreateAccount> {
                CreateAccountScreen(
                    onContinue = { navController.navigate( route = CreatePin )}
                )
            }
            composable<ImportAccount> {
                ImportAccountScreen()
            }
            composable<CreatePin> {
                CreatePinScreen(
                    onContinue = { navController.navigate( route = Wallet )}
                )
            }

           composable<Wallet> {
               WalletScreen()
           }
            composable<AddToken> {
                AddTokenScreen()
            }

            composable<Send> {
                SendScreen()
            }
            composable<Receive> {
                ReceiveScreen(modifier = Modifier)
            }
        }
    }
}