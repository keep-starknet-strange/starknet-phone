package com.example.walletapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import com.example.walletapp.ui.theme.WalletappTheme

class WalletActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            WalletappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WalletScreen()
                }
            }
        }
    }
}

@Composable
fun WalletScreen(modifier: Modifier = Modifier) {
    val networkList = listOf("Starknet Mainnet", "Test Networks")
    var selectedNetworkIndex by remember { mutableStateOf(0) }
    val context = (LocalContext.current as Activity)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color("#0C0C4F".toColorInt()))
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(), // Ensure Box takes full width of the parent
            contentAlignment = Alignment.Center // Center contents horizontally
        ) {
            SwitchNetwork(
                networkList,
                selectedNetworkIndex,
                modifier = Modifier,
                onItemClick = { index ->
                    selectedNetworkIndex = index
                }
            )
        }

        Text(
            text = "$11,625.48", // TODO(#82): load actual balance
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 70.dp)
        )
        Text(
            text = "0xfoo...123", // TODO(#82): load actual address
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // TODO(#82): load actual balance
        WalletCard(
            icon = painterResource(id = R.drawable.ic_ethereum),
            amount = "$11,625.7",
            exchange = 4.44,
            type = "ETH"
        )

        // TOOD(#82): load actual balance
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
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = "Receive",
                    fontFamily = FontFamily(Font(R.font.publicsans_bold)),
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
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = "Send",
                    fontFamily = FontFamily(Font(R.font.publicsans_bold)),
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
                contentDescription = null
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
                .clickable { isVisible = true }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color("#1B1B76".toColorInt()))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color("#5A5A5A".toColorInt()), RoundedCornerShape(30.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.token2),
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
            }
        }

        if (isVisible) {
            Popup(
                alignment = Alignment.TopCenter,
                properties = PopupProperties(excludeFromSystemGesture = true),
                onDismissRequest = { isVisible = false } // Close dropdown when clicking outside
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 70.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(140.dp)
                            .width(260.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color("#1B1B76".toColorInt()))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(1.dp, Color("#5A5A5A".toColorInt()), RoundedCornerShape(10.dp))
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
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
                                        .fillMaxWidth()
                                        .background(if (index == selectedIndex) Color("#6D6CA7".toColorInt()) else Color("#1B1B76".toColorInt()))
                                        .clickable {
                                            onItemClick(index)
                                            isVisible = false
                                        }
                                        .padding(vertical = 5.dp)
                                        .padding(bottom = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center

                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.token2),
                                            contentDescription = "Logo",
                                            modifier = Modifier.size(20.dp)
                                        )

                                        Spacer(modifier = Modifier.width(10.dp)) // Add horizontal space between the Image and Text

                                        Text(
                                            text = item,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
