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
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import kotlinx.coroutines.CoroutineScope

@Composable
fun ImportAccountScreen( onFinishAccountImport: () -> Unit) {
    var progress by remember { mutableStateOf(0.5f) }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color("#0C0C4F".toColorInt()),
                contentColor = Color.White,
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp),

                    ) {
                    // TODO(#100): add back navigation
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Backward  Arrow",
                        modifier = Modifier.padding(start = 8.dp),
                        tint = Color.White
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {

                        Text(
                            text = "Import existing wallet",
                            color = Color.White,
                            fontSize = 20.sp
                        )

                    }

                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color("#0C0C4F".toColorInt()))
                .padding(top = 30.dp, start = 16.dp, end = 16.dp )

        ) {

            Text(
                text = if (progress < 1.0f) "1 of 2" else "2 of 2",
                style = TextStyle(color = Color("#EC796B".toColorInt()), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, textAlign = TextAlign.Center )

            )

            Spacer(modifier = Modifier.height(5.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = Color("#EC796B".toColorInt())


            )

            if (progress < 1.0f) {
                PrivateKeyView(
                    modifier = Modifier.padding(top = 16.dp),
                    onNext = {
                        progress = 1.0f
                    }
                )
            } else {
                CreateNameView(modifier = Modifier.padding(top = 16.dp), onFinishAccountImport)
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun PrivateKeyView(modifier: Modifier = Modifier, onNext: () -> Unit) {
    val borderColor = Color("#1B1B76".toColorInt())
    var accountName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var openBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color("#0C0C4F".toColorInt()))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.padding(top = 5.dp, bottom = 16.dp)
        ) {
            Column {
                Text(
                    text = "Private key information",
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
                            "Enter private key",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
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

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Import")
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNameView(modifier: Modifier = Modifier, onFinishAccountImport: () -> Unit) {
    val borderColor = Color("#1B1B76".toColorInt())
    var accountName by remember { mutableStateOf("") }
    val context = (LocalContext.current as Activity)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color("#0C0C4F".toColorInt()))

    ) {
        Column {
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
                        "Enter account name",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFF1B1B76),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = borderColor,
                    unfocusedBorderColor = borderColor
                )
            )
        }





        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onFinishAccountImport,
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
                Spacer(modifier = Modifier.width(8.dp))
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


