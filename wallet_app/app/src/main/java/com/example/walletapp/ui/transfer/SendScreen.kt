package com.example.walletapp.ui.transfer

import android.app.Activity
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.walletapp.BuildConfig
import com.example.walletapp.R
import com.example.walletapp.ui.account.WalletViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.walletapp.utils.etherToWei
import com.example.walletapp.utils.isValidEthereumAddress
import com.swmansion.starknet.account.StandardAccount
import com.swmansion.starknet.data.types.CairoVersion
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.extensions.toFelt
import com.swmansion.starknet.provider.rpc.JsonRpcProvider
import com.swmansion.starknet.signer.StarkCurveSigner
import kotlinx.coroutines.future.await
import java.math.BigDecimal


@Composable
fun SendScreen(walletViewModel: WalletViewModel,navController: NavController) {

    val balances by walletViewModel.balances.collectAsState()
    val address= BuildConfig.ACCOUNT_ADDRESS
    val accountAddress = Felt.fromHex(address)
    val temporaryPrivateKey = BuildConfig.PRIVATE_KEY
    var amount by rememberSaveable { mutableStateOf("0.00") }
    var destinationWallet by rememberSaveable { mutableStateOf("") }
    val context = (LocalContext.current as Activity)
    var selectedToken by remember { mutableStateOf("ethereum") }

    suspend fun handleOnClick(amount: String, destinationWallet: String) {
        try {
            val amountValue = amount.toBigDecimalOrNull()
            val selectedBalance = balances
                .firstOrNull { map -> map[selectedToken] != null }
                ?.get(selectedToken) ?: 0.0

            if (!isValidEthereumAddress(destinationWallet)){
                Toast.makeText(
                    context,
                    "Invalid address",
                    Toast.LENGTH_LONG
                ).show()
            }
            else if (amountValue == null || amountValue.equals(BigDecimal(0))) {
                Toast.makeText(
                    context,
                    "Amount cannot be empty",
                    Toast.LENGTH_LONG
                ).show()
            }
            else if (selectedBalance == 0.0 || selectedBalance <= amount.toDouble()) {
                Toast.makeText(context, "Insufficient balance", Toast.LENGTH_LONG).show()
            }
            else {
                // TODO: This should be replaced once we have an account creation or account import feature
                val provider = JsonRpcProvider("https://starknet-sepolia.g.alchemy.com/starknet/version/rpc/v0_7/yPM6Zsiftub_GVxfcKGpDzoS1J89DrnZ")
                val privateKey = temporaryPrivateKey.toFelt
                val signer = StarkCurveSigner(privateKey)
                val chainId = provider.getChainId().sendAsync().await()
                val standardAccount = StandardAccount(
                    address = accountAddress,
                    signer = signer,
                    provider = provider,
                    chainId = chainId,
                    cairoVersion = CairoVersion.ONE,
                )
                val toAddress = destinationWallet.toFelt
                val amountUint256 = etherToWei(amountValue)
                walletViewModel.transferFunds(standardAccount, toAddress, amountUint256)
                Toast.makeText(context, "Transaction executed", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    var activateHandleOnClick by remember { mutableStateOf(false) }

    if (activateHandleOnClick) {
        LaunchedEffect(Unit) {
            handleOnClick(amount, destinationWallet)
            activateHandleOnClick = false
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color("#0C0C4F".toColorInt()))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Back Button
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(40.dp))

            // "From" label above the wallet address field (readonly)
            Text(
                text = "From",
                fontFamily = FontFamily(Font(R.font.publicsans_regular)),
                color = Color.White,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // From Address TextField (Read-only)
            TextField(
                value = address,
                onValueChange = {},
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.publicsans_regular)),
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp
                ),
                enabled = false, // Read-only
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "To",
                fontFamily = FontFamily(Font(R.font.publicsans_regular)),
                color = Color.White,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Wallet Address TextField
            TextField(
                value = destinationWallet,
                onValueChange = { newValue ->
                    destinationWallet = newValue
                },
                placeholder = { Text("Enter public address (0x)", color = Color.Gray) }, // Placeholder text
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.publicsans_regular)),
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, // Text keyboard for wallet address
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description Text
            Text(
                text = "Amount to send",
                fontFamily = FontFamily(Font(R.font.publicsans_regular)),
                color = Color.White,
                fontSize = 14.sp
            )

            // Dropdown button for selecting currency
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TokenDropdown(
                    selectedToken = selectedToken,
                    onTokenSelected = { token ->
                        selectedToken = token
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Amount Text
            TextField(
                value = amount,
                onValueChange = { newValue ->
                    amount = newValue
                },
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 40.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, // Number-only keyboard
                    imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.weight(1f))

            // Confirm Button
            Button(
                onClick = { activateHandleOnClick = true },
                colors = ButtonDefaults.buttonColors(containerColor  = Color("#1B1B76".toColorInt())),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp)
            ) {
                Text(text = "Confirm", color = Color.White)
            }
        }
    }
}

@Composable
fun TokenDropdown(
    selectedToken: String,
    onTokenSelected: (String) -> Unit,
    modifier: Modifier = Modifier

) {
    var expanded by remember { mutableStateOf(false) }
    val selectedTokenToTokenName = mapOf(
        "ethereum" to "ETH",
        "starknet" to "STRK"
    )

    Box(modifier = modifier) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E96)),
            modifier = Modifier.padding(8.dp)
        ) {
            if (selectedToken == "ethereum") {
                Image(
                    painter = painterResource(id = R.drawable.ic_ethereum),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.starknet_icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = selectedTokenToTokenName.getOrDefault(selectedToken, "ETH"), color = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                onTokenSelected("ethereum")
                expanded = false
            },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_ethereum),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                },
                text={
                    Text("ETH", color = Color.White)
                })
            DropdownMenuItem(onClick = {
                onTokenSelected("starknet")
                expanded = false
            },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.starknet_icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                },
                text={
                    Text("STRK", color = Color.White)
                })
        }
    }
}

