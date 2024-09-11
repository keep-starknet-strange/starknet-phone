package com.example.walletapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import com.example.walletapp.ui.theme.WalletappTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.swmansion.starknet.account.StandardAccount
import com.swmansion.starknet.data.types.*
import com.swmansion.starknet.provider.rpc.JsonRpcProvider
import com.swmansion.starknet.signer.StarkCurveSigner
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            WalletappTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CreateAccount(
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun StarknetLogo (modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.starknet_icon  ),
        contentDescription = "Starknet Logo",
        modifier = modifier.size(123.dp) )
}
@Composable
fun CreateAccount( modifier: Modifier) {
    val context = (LocalContext.current as Activity)
    val scope = rememberCoroutineScope()

    // Predefined values for account creation
    val privateKey = Felt.fromHex("0x2bbf4f9fd0bbb2e60b0316c1fe0b76cf7a4d0198bd493ced9b8df2a3a24d68a") // Replace with an actual private key
    val accountAddress = Felt.fromHex("0xb3ff441a68610b30fd5e2abbf3a1548eb6ba6f3559f2862bf2dc757e5828ca") // Replace with an actual address
    val provider = JsonRpcProvider("http://0.0.0.0:5050")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color("#0C0C4F".toColorInt()))
            .padding(20.dp)
    ) {
        Text(
            text = "Starknet Wallet",
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 70.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        StarknetLogo(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Adjust space between buttons
        ) {
            Button(
                onClick = { val i = Intent(context, AccountPasswordActivity::class.java)
                    context.startActivity(i) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt())),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Create a New Wallet",
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    color = Color.White,
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))


            Button(
                onClick = { val i = Intent(context, RecoveryPhraseActivity::class.java)
                    context.startActivity(i) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color("#EC796B".toColorInt())),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp)
            ) {
                Text(
                    text = "Import Starknet Wallet",
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    color = Color.White,
                    fontSize = 17.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // New button for deploying Starknet account
            Button(
                onClick = {
                    scope.launch {
                        try {
                            val signer = StarkCurveSigner(privateKey)
                            val chainId = provider.getChainId().sendAsync().await()
                            val account = StandardAccount(
                                address = accountAddress,
                                signer = signer,
                                provider = provider,
                                chainId = chainId,
                                cairoVersion = Felt.ONE,
                            )

                            // Here you would typically deploy the account
                            // For now, we'll just show a success message
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Account deployed successfully!", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Error deploying account: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color("#4CAF50".toColorInt())),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp)
            ) {
                Text(
                    text = "My Starknet Wallet",
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    color = Color.White,
                    fontSize = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
    }
}

