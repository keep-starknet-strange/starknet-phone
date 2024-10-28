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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
fun SendScreen(walletViewModel: WalletViewModel) {

    val balance by walletViewModel.balance.collectAsState()
    val address= BuildConfig.ACCOUNT_ADDRESS
    val accountAddress = Felt.fromHex(address)
    var amount by rememberSaveable { mutableStateOf("0.00") }
    var destinationWallet by rememberSaveable { mutableStateOf("") }
    val context = (LocalContext.current as Activity)

    suspend fun handleOnClick(amount: String, destinationWallet: String) {
        try {
            val amountValue = amount.toBigDecimalOrNull()
            val balanceValue = balance

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

            else if (balanceValue.isBlank() && amount.toDouble() <= balanceValue.toDouble()) {
                Toast.makeText(context, "Insufficient balance", Toast.LENGTH_LONG).show()
            }
            else {
                val provider = JsonRpcProvider("https://starknet-sepolia.g.alchemy.com/starknet/version/rpc/v0_7/yPM6Zsiftub_GVxfcKGpDzoS1J89DrnZ")
                val privateKey = "0x0647d2592a17db05fe60d079ca3336cc5ae984729a7ff63cef3ed1edf3a44bb4".toFelt

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
            }
        } catch (e: Exception) {
            println(e);
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
        /* TODO(34) send tokens */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color("#0C0C4F".toColorInt()))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
            Button(
                onClick = { /* Handle currency selection */ },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1E1E96)),
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_ethereum), // Replace with your Ethereum icon
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "ETH", color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White
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
                colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt())),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp)
            ) {
                Text(text = "Confirm", color = Color.White)
            }
        }
    }
}
