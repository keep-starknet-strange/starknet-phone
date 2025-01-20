package com.example.walletapp.ui.onboarding

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.walletapp.R
import com.example.walletapp.datastore.WalletStoreModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import okhttp3.Dispatcher


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePinScreen(onContinue: () -> Unit) {
    val context = (LocalContext.current as Activity)
    val borderColor = Color("#1B1B76".toColorInt())
    var passcode by remember { mutableStateOf("") }
    // var displayedPasscode by remember { mutableStateOf("") }
    var hiddenPasscode by remember { mutableStateOf("") }
    val maxDigits = 6
    val coroutineScope = rememberCoroutineScope()
    // For focus control
    val focusRequester = remember { FocusRequester() }
    val dataStore = WalletStoreModule(context)
    val hasAccountState = dataStore.hasAccount.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .background(Color("#0C0C4F".toColorInt()))
                .padding(top = 70.dp)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.starknet_icon),
                    contentDescription = "starknet",
                    modifier = Modifier
                        .size(36.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Starknet Wallet", style = TextStyle(
                        color = Color.White, fontSize = 25.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }



            Spacer(modifier = Modifier.height(120.dp))


            hasAccountState.value?.let { hasAccount ->
                Text(
                    text = if(hasAccount) "Enter PIN-code" else "Create PIN-code",
                    style = TextStyle(
                        color = Color.White, fontSize = 25.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

            }


            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "6 characters", style = TextStyle(
                    color = Color("#A3A3C1".toColorInt()), fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            )




            Spacer(modifier = Modifier.height(60.dp))


            OutlinedTextField(
                value = hiddenPasscode,
                onValueChange = {   newValue ->

                    val filterValue = newValue.filter { it.isDigit() || it == '*' }
                    if (filterValue.length <= passcode.length) {
                        passcode = filterValue
                        hiddenPasscode = filterValue
                    } else if (passcode.length < maxDigits) {
                        // Handle normal input
                        passcode = filterValue
                        hiddenPasscode = filterValue

                        coroutineScope.launch(Dispatchers.Default) {
                            delay(90L)
                            hiddenPasscode = newValue.dropLast(1) + "*"
                        }
                    }},
                placeholder = {
                    Text(
                        "Enter PIN Code",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth().padding(start=25.dp,end=25.dp)
                    .background(Color.Transparent).focusRequester(focusRequester),
                shape = RoundedCornerShape(8.dp),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number // Open numeric keyboard
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFF1B1B76),
                    focusedBorderColor = borderColor,
                    unfocusedBorderColor = borderColor,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
//            PasscodeCircles(passcode, hiddenPasscode, maxDigits)
//
//            Spacer(modifier = Modifier.height(48.dp))
//
//            NumericKeypad(onDigitClick = { digit ->
//                if (passcode.length < maxDigits) {
//                    passcode += digit
//                    hiddenPasscode += digit
//                    coroutineScope.launch {
//                        delay(500L)
//                        hiddenPasscode = hiddenPasscode.dropLast(1) + "*"
//                    }
//                }
//            }, onDeleteClick = {
//                if (passcode.isNotEmpty()) {
//                    passcode = passcode.dropLast(1)
//                    hiddenPasscode = hiddenPasscode.dropLast(1)
//                }
//            })




            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onContinue,
                contentPadding = ButtonDefaults.ContentPadding,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color("#EC796B".toColorInt()),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth().padding(16.dp)
                    .height(49.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Continue")
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Forward Arrow",
                        modifier = Modifier.padding(start = 8.dp),
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

        }

    }
}


@Composable
fun PasscodeCircles(passcode: String, hiddenPasscode: String, maxDigits: Int) {
    Row(
        modifier = Modifier
            .width(160.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (i in 0 until maxDigits) {
            val isFilled = i < passcode.length
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(BorderStroke(2.dp, Color("#A3A3C1".toColorInt())), CircleShape)
                    .background(
                        if (isFilled) Color("#A3A3C1".toColorInt()) else Color.Transparent,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (i < hiddenPasscode.length && hiddenPasscode[i] != '*') {
                    Text(
                        text = hiddenPasscode[i].toString(),
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun NumericKeypad(onDigitClick: (String) -> Unit, onDeleteClick: () -> Unit) {
    Column {
        for (row in listOf(listOf("1", "2", "3"), listOf("4", "5", "6"), listOf("7", "8", "9"))) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (digit in row) {
                    KeypadButton(digit) {
                        onDigitClick(digit)
                    }
                }

            }
            Spacer(modifier = Modifier.height(16.dp))

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            KeypadButton(".") {
                onDigitClick(".")
            }

            KeypadButton("0") {
                onDigitClick("0")
            }
            KeypadButton("⌫") {
                onDeleteClick()
            }
        }
    }
}


@Composable
fun KeypadButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color("#1B1B76".toColorInt()),
            contentColor = Color.White
        ),
        shape = CircleShape,
                modifier = Modifier
            .size(50.dp)

    ) {
        Text(
            text = text,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
