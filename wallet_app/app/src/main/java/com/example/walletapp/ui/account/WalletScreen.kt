package com.example.walletapp.ui.account

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.walletapp.BuildConfig
import com.example.walletapp.R
import com.example.walletapp.model.CoinData
import com.example.walletapp.utils.StarknetClient
import com.example.walletapp.utils.toDoubleWithTwoDecimal
import com.example.walletapp.utils.weiToEther
import com.swmansion.starknet.crypto.starknetKeccak
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.provider.exceptions.RpcRequestFailedException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale


@Composable
fun WalletScreen(
    onNewTokenPress: () -> Unit,
    onSendPress: () -> Unit,
    onReceivePress: () -> Unit,
    tokenViewModel: TokenViewModel
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Wallet(
            modifier = Modifier.padding(10.dp),
            onNewTokenPress = onNewTokenPress,
            onSendPress = onSendPress,
            onReceivePress = onReceivePress,
            tokenViewModel = tokenViewModel
        )
    }
}



@SuppressLint("MutableCollectionMutableState")
@Composable
fun Wallet(modifier: Modifier, onNewTokenPress: () -> Unit, onReceivePress: () -> Unit, onSendPress: () -> Unit,tokenViewModel: TokenViewModel) {
    val networkList = listOf("Starknet Mainnet", "Test Networks")
    var selectedNetworkIndex by remember { mutableStateOf(0) }
    val coinViewModel: CoinViewModel = viewModel()
    val tokens by tokenViewModel.tokens.observeAsState(initial = emptyList())
    val context = (LocalContext.current as Activity)
    val address= BuildConfig.ACCOUNT_ADDRESS
    val accountAddress = Felt.fromHex(address)
    val starknetClient = StarknetClient(BuildConfig.RPC_URL)

    var tokenImages by rememberSaveable { mutableStateOf<HashMap<String, String>>(hashMapOf()) }
    var balances by remember { mutableStateOf<List<HashMap<String, Double>>>(emptyList()) }
    val tokenIds = remember { mutableStateListOf<String>() }
    val coinsPrices by rememberSaveable { mutableStateOf<HashMap<String, Double>>(hashMapOf()) }
    var prices by rememberSaveable { mutableStateOf(mapOf<String, Double>()) }


    prices = coinViewModel.prices.value
    tokenImages=coinViewModel.tokenImages.value
    val errorMessage by coinViewModel.errorMessage

    LaunchedEffect(tokens) {
        if(tokens.isNotEmpty()){
            tokenIds.addAll(tokens.map { it.tokenId })
            coinViewModel.fetchTokenImages(tokenIds)
            try {
                coinViewModel.getTokenPrices(ids = tokenIds.joinToString(",") { it }, vsCurrencies = "usd")
                val balanceDeferred: List<Deferred<HashMap<String, Double>>> = tokens.map { token ->
                    async(Dispatchers.IO) {
                        try {
                            val balanceInWei = starknetClient.getBalance(accountAddress, token.contactAddress)
                            val balanceInEther = weiToEther(balanceInWei).toDoubleWithTwoDecimal()
                            hashMapOf(token.name to balanceInEther)
                        } catch (e: RpcRequestFailedException) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "${e.code}: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                            hashMapOf(token.name to 0.0)
                        }
                    }
                }
                // Wait for all balance fetching to complete
                balances = balanceDeferred.awaitAll()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
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


        val totalBalance = balances.flatMap { balanceMap ->
            balanceMap.entries.map { (token, balance) ->
                val price = coinsPrices[token] ?: 0.0
                balance * price
            }

        }.sum()
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        Text(
            text = formatter.format(totalBalance),
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
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp/2
        if(tokens.isNotEmpty() && tokenImages.isNotEmpty()){
            LazyColumn(modifier=Modifier.height(screenHeight)) {
                items(tokens.size) { index->
                    val tokenBalance=balances.firstOrNull { balanceMap ->
                        balanceMap.containsKey(tokens[index].name)
                    }?.get(tokens[index].name)?: 0.0
                    val price = coinsPrices[tokens[index].tokenId]?.let { it * tokenBalance }
                        ?.toDoubleWithTwoDecimal()
                        ?: 0.0
                    WalletCard(
                            imageUrl = tokenImages[tokens[index].tokenId]?:"",
                            amount = "$$price",
                            name=tokens[index].name,
                            balance = tokenBalance.toString(),
                            type = tokens[index].symbol.uppercase()
                        )

                }
            }

        }else{
            CircularProgressIndicator(modifier = Modifier.height(screenHeight).align(Alignment.CenterHorizontally))

        }


        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "+ New Token",
            fontFamily = FontFamily(Font(R.font.publicsans_bold)),
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .clickable {
                    onNewTokenPress()
                }
                .background(Color.Transparent)
                .align(Alignment.CenterHorizontally)
        )


        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onReceivePress,
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
                onClick = onSendPress,

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
fun WalletCard(imageUrl: String,name:String, amount: String, balance: String, type: String) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = imageUrl) // Set default image here
                        .apply { crossfade(true) }
                        .build()
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Color(0xFF1E1E96)
                        )
                ){Image(
                    painter = painter,// replace with your Ethereum icon
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(40.dp)
                )
                }

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = name.replaceFirstChar { it.uppercase() },
                    fontFamily = FontFamily(Font(R.font.publicsans_semibold)),
                    color = Color.White,
                    fontSize = 18.sp
                )

            }

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
                        text = balance,
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
