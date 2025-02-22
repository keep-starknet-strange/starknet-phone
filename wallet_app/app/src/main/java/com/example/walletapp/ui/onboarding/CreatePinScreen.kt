package com.example.walletapp.ui.onboarding

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.VisualTransformation
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
fun CreatePinScreen(onContinue: (passcode : String) -> Unit ,onError : () -> Unit) {
    val context = (LocalContext.current as Activity)
    val borderColor = Color("#1B1B76".toColorInt())
    var passcode by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    val maxDigits = 6
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
                    text = "Create PIN-code",
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
                value = passcode,
                onValueChange = {   newValue ->
                    val filterValue = newValue.filter { it.isDigit()}
                    if (filterValue.length <= maxDigits) {
                        passcode = filterValue
                    }
                                },
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
                visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation('*'),
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
                trailingIcon = {
                    if(passcode.isNotEmpty()){
                        Icon( if(visible) painterResource(R.drawable.eye_open) else painterResource(R.drawable.eye_close),
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp).clickable(){
                                visible=!visible

                            })
                    }

                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFF1B1B76),
                    focusedBorderColor = borderColor,
                    unfocusedBorderColor = borderColor,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )



            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick ={
                    if(passcode.length==maxDigits) onContinue(passcode) else onError()
                    Log.d("password",passcode)  },
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