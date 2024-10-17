package com.example.walletapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.walletapp.ui.theme.WalletappTheme
import com.example.walletapp.utils.StarknetClient
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.provider.exceptions.RpcRequestFailedException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AccountBalanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            WalletappTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AccountBalanceScreenView()
                }
            }
        }
    }

    @Composable
    fun AccountBalanceScreenView(){
         val context = (LocalContext.current as Activity)

         var accountAddress by remember { mutableStateOf("0x02dc260794e4c2eeae87b1403a88385a72c18a5844d220b88117b2965a8cf3a5") }

        var balance by remember { mutableStateOf("")}

        val scope = CoroutineScope(Dispatchers.IO)

        val starknetClient = StarknetClient(BuildConfig.RPC_URL)


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

                    scope.launch {
                        // Catch any errors and display message in the UI
                        try {
                            val accountAddress2 = Felt.fromHex( accountAddress)

                            // Get the balance of the account
                            val balancefinal = starknetClient.getEthBalance(accountAddress2)
                            Log.d("balance","${balancefinal}")
                            withContext(Dispatchers.Main) { balance= "${starknetClient.weiToEther(balancefinal)} ETH" }
                        } catch (e: RpcRequestFailedException) {
                            withContext(Dispatchers.Main) { Toast.makeText(applicationContext, "${e.code}: ${e.message}", Toast.LENGTH_LONG).show() }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) { Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show() }
                        }
                    }
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
            Text("Balance : ${(balance)}",color= Color.White,fontSize = 20.sp,fontWeight = FontWeight.Bold)


        }
    }
}