package com.example.walletapp.ui.onboarding

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(
    onContinue: () -> Unit
) {
    var progress by remember { mutableStateOf(0.5f) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Account", color = Color.White, fontSize = 20.sp) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Backward Arrow",
                        modifier = Modifier.padding(start = 8.dp),
                        tint = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color("#0C0C4F".toColorInt()),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color("#0C0C4F".toColorInt()))
                .padding(top = 30.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = if (progress < 1.0f) "1 of 2" else "2 of 2",
                style = TextStyle(
                    color = Color("#EC796B".toColorInt()),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(5.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = Color("#EC796B".toColorInt())
            )

            if (progress < 1.0f) {
                CreateWallet(
                    modifier = Modifier.padding(top = 16.dp),
                    onNext = {
                        progress = 1.0f
                    }
                )
            } else {
                GenerateKey(
                    modifier = Modifier.padding(top = 16.dp),
                    onContinue
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWallet(modifier: Modifier = Modifier, onNext: () -> Unit) {
    val borderColor = Color("#1B1B76".toColorInt())
    var accountName by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color("#0C0C4F".toColorInt()))
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier.padding(top = 5.dp, bottom = 16.dp)
        ) {
            Column(
            ) {

                Text(
                    text = "Create account name",
                    style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = Color.White,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = accountName,
                    onValueChange = { accountName = it },

                    placeholder = {
                        Text(
                            "Starknet",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                    ,
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color(0xFF1B1B76),
                        focusedBorderColor = borderColor,
                        unfocusedBorderColor = borderColor,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNext()  },
            contentPadding = ButtonDefaults.ContentPadding,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color("#EC796B".toColorInt()),
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateKey(modifier: Modifier = Modifier, onContinue: () -> Unit) {
    val scope = rememberCoroutineScope()
    var openBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color("#0C0C4F".toColorInt()))

    ) {
        Column(

        ) {
        }
        Box(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Generate private key",
                    style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = Color.White,
                    )
                )

            }
        }
        Button(
            onClick = {  openBottomSheet = true },
            contentPadding = ButtonDefaults.ContentPadding,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color("#EC796B".toColorInt()),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Forward Arrow",
                    modifier = Modifier.padding(start = 8.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate new private key")

            }
        }
    }

    if (openBottomSheet) {
        GeneratekeySheet(
            openBottomSheet = openBottomSheet,
            onDismissRequest = { openBottomSheet = false },
            sheetState = sheetState,
            scope = scope,
            onContinue = onContinue
        )
    }



}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratekeySheet(
    openBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
    onContinue: () -> Unit
) {

    val context = (LocalContext.current as Activity)

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = Color("#141462".toColorInt()),
            modifier= Modifier.height(364.dp),

            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .background(color = Color("#141462".toColorInt()))
            ) {

                Text( text = "Generating private key", style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 25.sp, textAlign = TextAlign.Center, color = Color.White))
                Spacer(modifier = Modifier.height(8.dp))
                Text( text = "Private key generated successfully", style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp, textAlign = TextAlign.Center, color = Color("#8D8DBB".toColorInt())))

                Spacer(modifier = Modifier.height(30.dp))

                Image(
                    painter = painterResource(id = R.drawable.approved),
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(93.dp)

                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onContinue,
                    contentPadding = ButtonDefaults.ContentPadding,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color("#EC796B".toColorInt()),
                        contentColor = Color.White
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp)
                ) {
                    Text(text = "Continue")
                }

            }
        }
    }


}

