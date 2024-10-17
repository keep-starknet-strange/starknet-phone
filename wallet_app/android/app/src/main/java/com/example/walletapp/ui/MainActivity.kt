package com.example.walletapp.ui

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
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
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
import com.example.walletapp.BuildConfig
import com.example.walletapp.ImportExistingKeyActivity
import com.example.walletapp.R
import com.example.walletapp.utils.StarknetClient
import com.example.walletapp.ui.activity.CreateAccountActivity
import com.example.walletapp.ui.theme.WalletappTheme
import com.example.walletapp.ui.components.TransparentButton
import com.example.walletapp.ui.components.StarknetLogo

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
fun CreateAccount( modifier: Modifier) {
    val context = (LocalContext.current as Activity)

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
            TransparentButton(
                label = "Create a New Wallet",
                onClick = { val i = Intent(context, CreateAccountActivity::class.java)
                    context.startActivity(i) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            TransparentButton(
                label = "Create a New Wallet",
                onClick = { val i = Intent(context, ImportExistingKeyActivity::class.java)
                    context.startActivity(i) },
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(15.dp))
    }
}

