package com.example.walletapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

class WalletActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            WalletappTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Wallet(
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }
        }
    }

}


    @Composable
    fun Wallet(modifier: Modifier) {
        val context = (LocalContext.current as Activity)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color("#0C0C4F".toColorInt()))
                .padding(20.dp)
        ) {
            Text(
                text = "$11,625.48",
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 70.dp)
            )
            Text(
                text = "0xfoo...123",
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            WalletCard(
                icon = painterResource(id = R.drawable.ic_ethereum),
                amount = "$11,625.7",
                exchange = 4.44,
                type = "ETH"
            )

            WalletCard(
                icon = painterResource(id = R.drawable.token2),
                amount = "$1.78",
                exchange = 4.44,
                type = "STRK"
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "+ New Token",
                fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(context, AddTokenActivity::class.java)
                        context.startActivity(intent)
                    }
                    .background(Color.Transparent)
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Button(
                onClick = {val intent = Intent(context, AccountBalanceActivity::class.java)
                    context.startActivity(intent)},
                colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt())),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.background(Color.Transparent)
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Get Account Balance", fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                    color = Color.White,
                    fontSize = 14.sp)
            }


            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val i = Intent(context, ReceiverActivity::class.java)
                        context.startActivity(i)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt())),
                    shape = RoundedCornerShape(15.dp),
                ) {
                    Text(
                        text = "Receive", fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
                Button(
                    onClick = {
                        val i = Intent(context, SendActivity::class.java)
                        context.startActivity(i)
                    },

                    colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt())),
                    shape = RoundedCornerShape(15.dp),
                ) {
                    Text(
                        text = "Send", fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }

    @Composable
    fun WalletCard(icon: Painter, amount: String, exchange: Double, type: String) {
        Card(
            backgroundColor = Color(0xFF1E1E96),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = icon, // replace with your Ethereum icon
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(modifier = Modifier, horizontalAlignment = Alignment.End) {
                    Text(
                        text = amount,
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    Row {
                        Text(
                            text = exchange.toString(),
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            color = Color.White,
                            fontSize = 10.sp
                        )
                        Text(
                            text = type,
                            fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                            color = Color.White,
                            fontSize = 10.sp
                        )

                    }

                }
            }
        }
    }








