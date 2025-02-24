package com.example.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.demo.ui.theme.WalletsdkTheme
import com.snphone.snwalletsdk.SNWalletSDK
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : ComponentActivity() {
    private lateinit var sdk: SNWalletSDK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContext = applicationContext
        sdk = SNWalletSDK(appContext)

        setContent {
            val darkColorScheme = darkColorScheme(
                primary = Color(0xFF1E2749),
                onPrimary = Color.White,
                secondary = Color(0xFF00BFA5),
                onSecondary = Color.Black,
                background = Color(0xFF121212),
                onBackground = Color.White,
                surface = Color(0xFF1E1E1E),
                onSurface = Color.White,
                error = Color(0xFFCF6679),
                onError = Color.Black
            )
            MaterialTheme(colorScheme = darkColorScheme) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MainContent(modifier = Modifier.padding(innerPadding)) {
                            lifecycleScope.launch {
                                signTransaction()
                            }
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
fun MainContent(modifier: Modifier = Modifier, onSignTransaction: () -> Unit) {
    var showConfirmation by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var amountFrom by remember { mutableStateOf("1") }
    var amountTo by remember { mutableStateOf("") }
    val ethToStrkRate = remember { BigDecimal("1500.00") } // 1 ETH = 1500 STRK (example rate)
    val context = LocalContext.current

    // Calculate STRK amount when ETH amount changes
    LaunchedEffect(amountFrom) {
        if (amountFrom.isNotEmpty()) {
            val ethAmount = BigDecimal(amountFrom)
            val strkAmount = ethAmount.multiply(ethToStrkRate).setScale(2, RoundingMode.HALF_UP)
            amountTo = strkAmount.toPlainString()
        } else {
            amountTo = ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Amount From
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = amountFrom,
                onValueChange = {
                    // Only allow numeric input
                    if (it.isEmpty() || it.matches(Regex("[0-9]+(\\.[0-9]+)?"))) {
                        amountFrom = it
                    }
                },
                label = { Text("Amount From") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "ETH")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Amount To Receive
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = amountTo,
                onValueChange = { /* Read-only */ },
                label = { Text("Amount To Receive") },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "STRK")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Swap Button
        Button(
            onClick = {
                if (amountFrom.isNotEmpty() && amountTo.isNotEmpty()) {
                    coroutineScope.launch {
                        // Simulate sending the transaction
                        delay(2000) // Simulate a 2-second delay
                        showConfirmation = true
                        onSignTransaction()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Please enter an amount to swap",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Swap")
        }
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Swap Successful") },
            text = { Text("Your swap of $amountFrom ETH for $amountTo STRK has been completed successfully.") },
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