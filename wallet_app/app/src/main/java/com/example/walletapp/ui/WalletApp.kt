package com.example.walletapp.ui

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.walletapp.datastore.WalletStoreModule
import com.example.walletapp.ui.account.AddTokenScreen
import com.example.walletapp.ui.account.EnterPinScreen
import com.example.walletapp.ui.account.TokenViewModel
import com.example.walletapp.ui.account.WalletScreen
import com.example.walletapp.ui.account.WalletViewModel
import com.example.walletapp.ui.activity.FinalizeAccountCreationScreen
import com.example.walletapp.ui.onboarding.CreateAccountScreen
import com.example.walletapp.ui.onboarding.CreatePinScreen
import com.example.walletapp.ui.onboarding.ImportAccountScreen
import com.example.walletapp.ui.onboarding.OnboardingScreen
import com.example.walletapp.ui.theme.WalletappTheme
import com.example.walletapp.ui.transfer.ReceiveScreen
import com.example.walletapp.ui.transfer.SendScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
@Serializable
object EnterPin
@Serializable
object FinalizeAccountCreation

// token transfer flow
@Serializable
object Send
@Serializable
object Receive

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WalletApp(tokenViewModel: TokenViewModel) {
    val walletViewModel: WalletViewModel = viewModel()
    val context = LocalContext.current
    val dataStore = WalletStoreModule(context)
    val hasAccountState = dataStore.hasAccount.collectAsState(initial = null)
    hasAccountState.value?.let { hasAccount ->
    WalletappTheme {

        val startDestination = if (hasAccount) EnterPin else Onboarding

        val navController = rememberNavController()
        NavHost(navController, startDestination = startDestination) {

            composable<Onboarding> {
                OnboardingScreen(
                    onNavigateCreateAccount = { navController.navigate( route = CreateAccount ) },
                    onNavigateImportAccount = { navController.navigate( route = ImportAccount ) }
                )
            }
            composable<CreateAccount> {
                CreateAccountScreen(
                    onContinue = { navController.navigate( route = FinalizeAccountCreation )},
                    onBackButtonPressed = {navController.navigateUp()}
                )
            }
            composable<FinalizeAccountCreation> {
                FinalizeAccountCreationScreen(
                    onContinue = { navController.navigate( route = CreatePin ) },
                    onBackButtonPressed = { navController.navigateUp() }
                )
            }
            composable<ImportAccount> {
                ImportAccountScreen(
                    onFinishAccountImport = { navController.navigate( route = CreatePin ) },
                    onBackButtonPressed = { navController.navigateUp() }
                )
            }
            composable<CreatePin> {
                CreatePinScreen(
                    onContinue = {
                        navController.navigate( route = Wallet )
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.setHasAccount(true)
                        }
                    },
                    onError = {
                        Toast.makeText(context,"Enter 6 digit pin", Toast.LENGTH_LONG).show()
                    }
                )
            }
            composable<EnterPin> {
                EnterPinScreen(
                    onContinue = {
                            navController.navigate( route = Wallet )
                    }
                )
            }

           composable<Wallet> {
               WalletScreen(
                   onNewTokenPress = { navController.navigate( route = AddToken ) },
                   onReceivePress = { navController.navigate( route = Receive ) },
                   onSendPress = { navController.navigate( route = Send ) },
                   tokenViewModel = tokenViewModel,
                   walletViewModel = walletViewModel,
                   onBack = {
                       val activity = (navController.context as? Activity)
                       activity?.finish()
                   }
               )
           }
            composable<AddToken> {
                AddTokenScreen(
                    tokenViewModel=tokenViewModel,
                    onConfirm = { navController.navigateUp() },
                    navController
                )
            }

            composable<Send> {
                SendScreen(walletViewModel,navController)
            }
            composable<Receive> {
                ReceiveScreen(modifier = Modifier,navController)
            }
        }
    }
        }
}