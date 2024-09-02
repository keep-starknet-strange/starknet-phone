package com.example.walletapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import java.math.BigDecimal
import com.example.walletapp.ui.theme.WalletappTheme

class AccountBalanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            WalletappTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AccountBalanceScreenView(
                    )
                }
            }
        }
    }

    @Composable
    fun AccountBalanceScreenView(){

        val rpcUrl = "https://mainnet.infura.io/v3/YOUR_INFURA_PROJECT_ID"  // Replace with your RPC URL
        val accountAddress = "0xYourHardcodedAccountAddress" // account Address


        // Call the balance retrieval function
        val balance = getAccountBalance(rpcUrl, accountAddress)?.toPlainString() ?: "0.0"

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color("#0C0C4F".toColorInt()))
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Your Account Balance",
                fontFamily = FontFamily(Font(R.font.publicsans_regular)),
                color = Color.White,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$$balance",
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 70.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = accountAddress,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )



        }
    }

    fun getAccountBalance(rpcUrl: String, accountAddress: String): BigDecimal? {
        val web3j = Web3j.build(HttpService(rpcUrl))
        return try {
            val ethGetBalance = web3j.ethGetBalance(accountAddress, org.web3j.protocol.core.DefaultBlockParameterName.LATEST).send()
            val balanceInWei = ethGetBalance.balance
            Convert.fromWei(balanceInWei.toString(), Convert.Unit.ETHER)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}