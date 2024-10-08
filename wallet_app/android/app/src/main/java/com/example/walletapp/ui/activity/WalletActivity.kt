package com.example.walletapp.ui.activity

import StarknetClient
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.service.quickaccesswallet.WalletCard
import android.widget.Toast
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.provider.exceptions.RpcRequestFailedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.text.DecimalFormat


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
        val networkList = listOf("Starknet Mainnet", "Test Networks")
        var selectedNetworkIndex by remember { mutableStateOf(0) }
        val context = (LocalContext.current as Activity)
        val address=BuildConfig.ACCOUNT_ADDRESS
        val accountAddress = Felt.fromHex(address)
        val starknetClient = StarknetClient(BuildConfig.RPC_URL)
        var balance by remember { mutableStateOf("") }

        val coinViewModel: CoinViewModel = viewModel()

        val coinsPrices : HashMap<String,String> = rememberSaveable {
            hashMapOf()
        }

        val prices by coinViewModel.prices
        val errorMessage by coinViewModel.errorMessage

        LaunchedEffect(Unit) {
            coinViewModel.getTokenPrices(ids = "starknet,ethereum", vsCurrencies = "usd")  // Fetch starknet and bitcoin prices in USD
        }

        LaunchedEffect (Unit){
            try {
                // Get the balance of the account
                val getBalance = starknetClient.getEthBalance(accountAddress)
                withContext(Dispatchers.Main) {
                    balance = starknetClient.weiToEther(getBalance).toDoubleWithTwoDecimal()
                }
            } catch (e: RpcRequestFailedException) {
                withContext(Dispatchers.Main) { Toast.makeText(context, "${e.code}: ${e.message}", Toast.LENGTH_LONG).show() }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { Toast.makeText(context, e.message, Toast.LENGTH_LONG).show() }
            }
        }
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Column(modifier = Modifier.padding(16.dp)) {
                prices.forEach { (token, price) ->
                    coinsPrices[token] = price.toDoubleWithTwoDecimal()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

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
                text ="$11,625.48",
                fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 70.dp)
            )
            Text(
                text = address.take(10) + ".....",
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))


            WalletCard(
                icon = painterResource(id = R.drawable.ic_ethereum),
                amount = coinsPrices["ethereum"]?.let { "$ $it" } ?: "",
                exchange = balance,
                type = "ETH"
            )

            // TOOD(#82): load actual balance
            WalletCard(
                icon = painterResource(id = R.drawable.token2),
                amount = coinsPrices["starknet"]?.let { "$ $it" } ?: "",
                exchange ="4.44",
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
// Function to format BigDecimal to Double with 2 decimal places
fun BigDecimal.toDoubleWithTwoDecimal(): String {
    val decimalFormat = DecimalFormat("#.00")
    return decimalFormat.format(this.toDouble())
}
fun Double.toDoubleWithTwoDecimal(): String {
    val decimalFormat = DecimalFormat("#.00")
    val formattedValue = decimalFormat.format(this)
    return if (this < 1) "0$formattedValue" else formattedValue
}

    @Composable
    fun WalletCard(icon: Painter, amount: String, exchange: String, type: String) {
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
                            text = exchange,
                            fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Spacer(modifier=Modifier.width(2.dp))
                        Text(
                            text = type,
                            fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                            color = Color.White,
                            fontSize = 12.sp
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
                ){
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
               ){
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
                               .padding(4.dp),
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
