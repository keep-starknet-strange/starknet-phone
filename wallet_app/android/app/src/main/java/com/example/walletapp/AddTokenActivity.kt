package com.example.walletapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import com.example.walletapp.ui.theme.WalletappTheme

class AddTokenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            WalletappTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AddTokenScreenView(modifier = Modifier.padding(10.dp))
                }
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
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color("#1B1B76".toColorInt()),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                textColor = Color.Black,
                placeholderColor = Color("#A9A9A9".toColorInt())
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun AddTokenScreenView(modifier: Modifier) {
    val contactAddress = rememberSaveable { mutableStateOf("") }
    val name = rememberSaveable { mutableStateOf("") }
    val symbol = rememberSaveable { mutableStateOf("") }
    val decimals = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color("#0C0C4F".toColorInt()))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
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
            onClick = { /* Handle confirm action */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt())),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
        ) {
            Text(text = "Confirm", color = Color.White)
        }
    }
}
