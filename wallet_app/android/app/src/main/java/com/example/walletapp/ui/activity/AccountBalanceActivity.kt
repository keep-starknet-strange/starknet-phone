package com.example.walletapp.ui.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walletapp.data.repository.StarknetRepository
import com.example.walletapp.ui.theme.WalletappTheme
import java.math.BigDecimal
import java.math.RoundingMode


class AccountBalanceActivity : ComponentActivity() {
    private lateinit var viewModelFactory: StarknetViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Create the repository and factory
        val repository = StarknetRepository()  // Initialize your repository here
        viewModelFactory = StarknetViewModelFactory(repository)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            WalletappTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AccountBalanceScreenView(viewModelFactory = viewModelFactory
                    )
                }
            }
        }
    }

    @Composable
    fun AccountBalanceScreenView(viewModelFactory: StarknetViewModelFactory){
        val context = (LocalContext.current as Activity)
        val viewModel: StarknetViewModel = viewModel(factory = viewModelFactory)

        val contractAddress by remember { mutableStateOf("0x049d36570d4e46f48e99674bd3fcc84644ddd6b96f7c741b1562b82f9e004dc7") }
        var accountAddress by remember { mutableStateOf("0x02dc260794e4c2eeae87b1403a88385a72c18a5844d220b88117b2965a8cf3a5") }

        val balance by viewModel.balanceLiveData.observeAsState("...")
        val error by viewModel.errorLiveData.observeAsState("")

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color("#0C0C4F".toColorInt()))
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            TextField(
                value = accountAddress,
                onValueChange = { accountAddress = it },
                label = { Text("Account Address",
                    fontSize = 16.sp,
                        color = Color.White,
                    modifier=Modifier.padding(10.dp),
                    fontWeight = FontWeight.Bold) },
                modifier=Modifier.padding(15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color("#ffffff".toColorInt()),
                    disabledTextColor = Transparent,
                    backgroundColor = Transparent,
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent,
                    disabledIndicatorColor = Transparent
                )

            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(){
                Button(onClick = {
                    viewModel.fetchAccountBalance(contractAddress, accountAddress)
                }) {
                    Text("Get Balance")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    context.finish()
                }) {
                    Text("Back to home")
                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            if (error.isNotEmpty()) {
                Text("Error: $error",color= MaterialTheme.colors.error,fontSize = 20.sp,fontWeight = FontWeight.Bold)
            } else {
                Text("Balance : ${ConvertHexToBalance(balance)}",color= Color.White,fontSize = 20.sp,fontWeight = FontWeight.Bold)
            }


        }
    }


    fun ConvertHexToBalance(hex: String): String {
        return try {
            // Remove "0x" prefix if present
            val cleanedHex = hex.removePrefix("0x")

            // Convert hex string to BigInteger
            val balanceBigInt = cleanedHex.toBigInteger(16)

            // If StarkNet uses a base unit similar to Ethereum's wei, you might need to adjust the balance
            // For this example, we'll assume the balance is in the smallest unit and needs to be converted

            // Convert balance to a decimal format (adjust the divisor based on StarkNet's unit)
            val balanceDecimal = BigDecimal(balanceBigInt)
                .divide(BigDecimal("1000000000000000000"), 18, RoundingMode.HALF_UP)

            balanceDecimal.stripTrailingZeros().toPlainString() // Return balance without scientific notation
        } catch (e: Exception) {
            "Invalid Balance" // Handle invalid hex strings or other exceptions
        }
    }

}