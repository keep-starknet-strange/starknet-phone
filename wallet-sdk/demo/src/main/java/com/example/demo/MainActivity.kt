package com.example.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.demo.ui.theme.WalletsdkTheme
import com.snphone.snwalletsdk.SNWalletSDK
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var sdk: SNWalletSDK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContext = applicationContext
        sdk = SNWalletSDK(appContext)

        setContent {
            WalletsdkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding)
                    ) { transactionData ->
                        lifecycleScope.launch {
                            signTransaction()
                        }
                    }
                }
            }
        }
    }

    private suspend fun signTransaction() {
        try {
            sdk.sendTransaction()
        } catch (e: Exception) {
            // Handle the exception
            Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(modifier: Modifier = Modifier, onSignTransaction: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                showDialog = true
            }
        ) {
            Text(text = "Sign Transaction")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Transaction") },
            text = { Text("Are you sure you want to send this transaction?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        coroutineScope.launch {
                            // Simulate sending the transaction
                            delay(2000) // Simulate a 2-second delay
                            showConfirmation = true
                            onSignTransaction("transaction data")
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Transaction Sent") },
            text = { Text("Your transaction has been sent successfully.") },
            confirmButton = {
                Button(onClick = { showConfirmation = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WalletsdkTheme {
        MainContent() {}
    }
}