package com.example.walletapp.ui.onboarding

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.walletapp.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay


@Composable
fun CreatePinScreen(onContinue: () -> Unit) {
    val context = (LocalContext.current as? Activity)
    var passcode by remember { mutableStateOf("") }
    var hiddenPasscode by remember { mutableStateOf("") }
    var isConfirming by remember {mutableStateOf(false)}
    var createdPasscode by remember { mutableStateOf("") }
    var isError by remember{ mutableStateOf(false) }
    val maxDigits = 6
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
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


            Spacer(modifier = Modifier.height(93.dp))



            Text(
                text = if (isConfirming)"Confirm PIN-code" else "Create PIN-code",
                style = TextStyle(
                    color = Color.White, fontSize = 25.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "6 characters", style = TextStyle(
                    color = Color("#A3A3C1".toColorInt()), fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            //Error Message when pins do not match
            if (isError){
                Text(
                    text = "PINs don't match. Please try again.",
                    style = TextStyle(
                        color = Color.Red,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }




            Spacer(modifier = Modifier.height(31.dp))
            PasscodeCircles(passcode, hiddenPasscode, maxDigits, isError)

            Spacer(modifier = Modifier.height(48.dp))

            NumericKeypad(onDigitClick = { digit ->
                if (passcode.length < maxDigits) {
                    passcode += digit
                    hiddenPasscode += digit
                    coroutineScope.launch {
                        delay(500L)
                        hiddenPasscode = hiddenPasscode.dropLast(1) + "*"
                    }
                }

                if(passcode.length == maxDigits){
                    if (isConfirming){
                        if(passcode == createdPasscode){onContinue()}
                        else{
                            isError = true
                            passcode = ""
                            hiddenPasscode = ""
                            isConfirming = false
                            createdPasscode = ""
                            coroutineScope.launch{
                                delay(5000)
                                isError = false
                            }


                        }
                    } else {
                        createdPasscode = passcode
                        passcode = ""
                        hiddenPasscode = ""
                        isConfirming = true
                    }
                }

            }, onDeleteClick = {
                if (passcode.isNotEmpty()) {
                    passcode = passcode.dropLast(1)
                    hiddenPasscode = hiddenPasscode.dropLast(1)
                }
            })




            Spacer(modifier = Modifier.height(102.dp))

            Button(
                onClick = onContinue,
                contentPadding = ButtonDefaults.ContentPadding,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color("#EC796B".toColorInt()),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
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

        }

    }
}


@Composable
fun PasscodeCircles(passcode: String, hiddenPasscode: String, maxDigits: Int, isError: Boolean) {
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
                    .border(
                        BorderStroke(
                            2.dp,
                            if (isError) Color.Red else Color("#A3A3C1".toColorInt())
                        ),
                        CircleShape
                    )
                    .background(
                        when {
                            isError && isFilled -> Color.Red
                            isFilled -> Color("#A3A3C1".toColorInt())
                            else -> Color.Transparent
                        },
//                        if (isFilled) Color("#A3A3C1".toColorInt()) else Color.Transparent,
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
        colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt()), contentColor = Color.White),
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