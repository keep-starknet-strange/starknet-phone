package com.example.walletapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.walletapp.ui.account.AddTokenScreen
import com.example.walletapp.ui.account.WalletScreen
import com.example.walletapp.ui.activity.FinalizeAccountCreationScreen
import com.example.walletapp.ui.onboarding.CreateAccountScreen
import com.example.walletapp.ui.onboarding.CreatePinScreen
import com.example.walletapp.ui.onboarding.ImportAccountScreen
import com.example.walletapp.ui.onboarding.OnboardingScreen
import com.example.walletapp.ui.theme.WalletappTheme
import com.example.walletapp.ui.transfer.ReceiveScreen
import com.example.walletapp.ui.transfer.SendScreen
import kotlinx.serialization.Serializable

// Create DataStore instance for saving account preferences
private val Context.dataStore by preferencesDataStore(name = "account_preferences")

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
object FinalizeAccountCreation

// token transfer flow
@Serializable
object Send
@Serializable
object Receive

@Composable
fun WalletApp() {
    WalletappTheme {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        // Key for storing account creation status
        val hasAccountKey = booleanPreferencesKey("has_account")

        // State to track whether an account has been created
        var hasAccount by remember { mutableStateOf(false) }

        // Launch effect to load account creation status from DataStore
        LaunchedEffect(Unit) {
            context.dataStore.data.catch { exception ->
                    // Handle exceptions during data reading, such as IO exceptions
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }.map { preferences ->
                    // Retrieve the account creation status, defaulting to false if not set
                    preferences[hasAccountKey] ?: false
                }.collect { value ->
                    hasAccount = value
                }
        }
        
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
                    onContinue = { navController.navigate( route = FinalizeAccountCreation )}
                )
            }
            composable<FinalizeAccountCreation> {
                FinalizeAccountCreationScreen(
                    onContinue = { navController.navigate( route = CreatePin )}
                )
            }
            composable<ImportAccount> {
                ImportAccountScreen(
                    onFinishAccountImport = { navController.navigate( route = CreatePin ) }
                )
            }
            composable<CreatePin> {
                CreatePinScreen(
                    onContinue = { navController.navigate( route = Wallet )}
                )
            }

           composable<Wallet> {
               WalletScreen(
                   onNewTokenPress = { navController.navigate( route = AddToken ) },
                   onReceivePress = { navController.navigate( route = Receive ) },
                   onSendPress = { navController.navigate( route = Send ) }
               )
           }
            composable<AddToken> {
                AddTokenScreen(
                    onConfirm = { navController.navigateUp() }
                )
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