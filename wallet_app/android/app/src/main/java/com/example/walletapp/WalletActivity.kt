package com.example.walletapp
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties


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
        val networkList = listOf("Starknet Mainnet", "Starknet Testnet")
        val selectedNetworkIndex = 0 
        val context = (LocalContext.current as Activity)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color("#0C0C4F".toColorInt()))
                .padding(20.dp)
        ) {

              Box(

               ){
                   SwitchNetwork(networkList, selectedNetworkIndex,
                       modifier = Modifier,
                       onItemClick = { index ->
                           println("Selected network: ${networkList[index]}")
                       })
               }
           }


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






@Composable
fun SwitchNetwork(
    itemList: List<String>,
    selectedIndex: Int,
    modifier: Modifier,
    onItemClick: (Int) -> Unit
) {

    var isVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Dropdown trigger button
        Box(
            modifier = modifier
                .height(44.dp)
                .width(250.dp)
                .background(Color(0xFF1B1B76))
                .clip(RoundedCornerShape(48.dp))
                .border(1.dp, Color(0xFF5A5A5A))
                .clickable { isVisible = true }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.starknet_icon),
                    contentDescription = "Logo",
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = itemList[selectedIndex],
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.White
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = Color.White
                )
            }
        }


        // Dropdown menu
        if (isVisible) {
            Popup(
                alignment = Alignment.TopCenter,
                properties = PopupProperties(excludeFromSystemGesture = true),
                onDismissRequest = { isVisible = false } // Close dropdown when clicking outside
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 70.dp)
                        .height(160.dp)
                        .width(300.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFF5A5A5A))
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = { isVisible = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Popup",
                                    tint = Color.White
                                )
                            }
                        }

                        itemList.forEachIndexed { index, item ->

                            Box(
                                modifier = Modifier
                                    .background(Color.Green)
                                    .fillMaxWidth()
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = {
                                                onItemClick(index)
                                                isVisible = false
                                            }
                                        )
                                    }
                                    .hoverable(remember { MutableInteractionSource() })
                                    .background( Color(0xFF1B1B50))
                                    .clickable { onItemClick(index) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

    }
}



