package com.example.walletapp.ui.account
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.walletapp.R
import com.example.walletapp.model.Token
import com.example.walletapp.model.TokenIdsResponse
import com.swmansion.starknet.data.types.Felt

@Composable
fun AddTokenScreen(tokenViewModel:TokenViewModel, onConfirm: () -> Unit, navController: NavController,
) {
    
    Surface(modifier = Modifier.fillMaxSize()) {
        val contactAddress = rememberSaveable { mutableStateOf("") }
        val name = rememberSaveable { mutableStateOf("") }
        val symbol = rememberSaveable { mutableStateOf("") }
        val decimals = rememberSaveable { mutableStateOf("") }
        val context = (LocalContext.current as Activity)
        val coinViewModel: CoinViewModel = viewModel()
        var ids by rememberSaveable { mutableStateOf(TokenIdsResponse()) }
        ids=coinViewModel.tokenIds.value

        LaunchedEffect(Unit) {
            coinViewModel.getTokenIds()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color("#0C0C4F".toColorInt()))
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
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
            
            Text(
                text = "Add Token",
                fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                color = Color.White,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                SimpleTextField(
                    value = contactAddress.value,
                    onValueChange = { contactAddress.value = it },
                    label = "Contact Address",
                    placeholder = "0x123"
                )
                Spacer(modifier = Modifier.height(16.dp))
                SimpleTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = "Name",
                    placeholder = "Name"
                )
                Spacer(modifier = Modifier.height(16.dp))
                SimpleTextField(
                    value = symbol.value,
                    onValueChange = { symbol.value = it },
                    label = "Symbol",
                    placeholder = "TOK"
                )
                Spacer(modifier = Modifier.height(16.dp))
                SimpleTextField(
                    value = decimals.value,
                    onValueChange = { decimals.value = it },
                    label = "Decimals",
                    placeholder = "18"
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val decimalValue = decimals.value.toIntOrNull()
                    val addressPattern = Regex("^0x[a-fA-F0-9]{64}$")
                    if (!addressPattern.matches(contactAddress.value)) {
                        Toast.makeText(context, "Please enter a valid address", Toast.LENGTH_LONG).show()
                    }
                    else if (name.value.isEmpty()) {
                        Toast.makeText(context, "Please enter name", Toast.LENGTH_LONG)
                            .show()
                    }
                    else if (symbol.value.isEmpty()) {
                        Toast.makeText(context, "Please enter symbol", Toast.LENGTH_LONG)
                            .show()
                    } else if (decimalValue == null || decimalValue !in 0..18) {
                        Toast.makeText(context, "Please enter valid decimal", Toast.LENGTH_LONG)
                            .show()
                    }else{
                        onConfirm()
                        // Save data to local storage here
                        val address=Felt.fromHex(contactAddress.value)
                        val tokenId= ids.find { it.name.equals(name.value, ignoreCase = true) }?.id?:""
                        val newToken = Token(
                            contactAddress = address,
                            name = name.value,
                            symbol = symbol.value,
                            decimals = decimalValue,
                            tokenId =tokenId
                        )
                        tokenViewModel.insertToken(newToken) // Insert the new token
                        Toast.makeText(context, "Token added!", Toast.LENGTH_LONG).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color("#1B1B76".toColorInt())
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp)
            ) {
                Text(text = "Confirm", color = Color.White)
            }
        }
    }
}

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.publicsans_regular)),
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color("#1B1B76".toColorInt()),
                focusedContainerColor = Color("#1B1B76".toColorInt()),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedPlaceholderColor = Color("#A9A9A9".toColorInt()),
                focusedPlaceholderColor = Color("#A9A9A9".toColorInt())
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp
            )
        )
    }
}


