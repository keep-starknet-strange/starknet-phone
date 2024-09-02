package com.example.walletapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import com.example.walletapp.ui.theme.WalletappTheme

class AccountPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            WalletappTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AccountPasswordScreenView(
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun AccountPasswordScreenView(modifier: Modifier){
        val (password, setPassword) = remember { mutableStateOf("") }
        val (confirmPassword, setConfirmPassword) = remember { mutableStateOf("") }
        val context = (LocalContext.current as Activity)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color("#0C0C4F".toColorInt()))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { }) {
                    Image(
                        painter = painterResource(R.drawable.arrow_back_ios),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "Create Wallet Password",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    modifier = Modifier.weight(2f),
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Include letters, numbers & symbol for a strong password ",
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color.White,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = password,
                onValueChange = { newValue -> setPassword(newValue) },
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    color = Color.White,
                    fontSize = 15.sp
                ),
                placeholder = {
                    Text(
                        text = "Enter your password *",
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        color = Color.White,
                        fontSize = 15.sp
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, // Number-only keyboard
                    imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = confirmPassword,
                onValueChange = { newValue -> setConfirmPassword(newValue) },
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    color = Color.White,
                    fontSize = 15.sp
                ),
                placeholder = {
                    Text(
                        text = "Confirm Password *",
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        color = Color.White,
                        fontSize = 15.sp
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, // Number-only keyboard
                    imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painterResource(id = R.drawable.progress_1),
                contentDescription ="progress",
                modifier = Modifier.fillMaxWidth().height(6.dp))

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { val i = Intent(context, RecoveryPhraseActivity::class.java)
                    context.startActivity(i) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt())),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp)

            ) {
                Text(text = "Next", color = Color.White)
            }
            Spacer(modifier = Modifier.height(15.dp))
        }

    }
}
