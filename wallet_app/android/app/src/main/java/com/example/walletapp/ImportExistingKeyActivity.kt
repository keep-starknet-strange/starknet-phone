package com.example.walletapp
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.walletapp.R
import com.example.walletapp.ui.theme.WalletappTheme
import kotlinx.coroutines.CoroutineScope

class ImportExistingKeyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WalletappTheme {
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
                            CreateNameView(modifier = Modifier.padding(top = 16.dp))
                        }


                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)

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
                        backgroundColor = Color(0xFF1B1B76),
                        focusedBorderColor = borderColor,
                        textColor = Color.White,
                        unfocusedBorderColor = borderColor
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {  openBottomSheet = true },
            contentPadding = ButtonDefaults.ContentPadding,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color("#EC796B".toColorInt()), contentColor = Color.White),
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



        if (openBottomSheet) {
            ConfirmSheet(
                onNext = onNext,
                openBottomSheet = openBottomSheet,
                onDismissRequest = { openBottomSheet = false },
                sheetState = sheetState,
                scope = scope
            )
        }
    }
}



@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateNameView(modifier: Modifier = Modifier) {
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
                    backgroundColor = Color(0xFF1B1B76),
                    textColor = Color.White,
                    focusedBorderColor = borderColor,
                    unfocusedBorderColor = borderColor
                )
            )
        }





        Spacer(modifier = Modifier.weight(1f))

        Button(
             onClick = { val i = Intent(context, CreatePinActivity::class.java)
                context.startActivity(i) },
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


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ConfirmSheet(
    onNext: () -> Unit,
    openBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    sheetState: SheetState ,
    scope: CoroutineScope
) {

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
                    onClick = { onNext() },
                    contentPadding = ButtonDefaults.ContentPadding,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color("#EC796B".toColorInt()), contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp)
                ) {
                    Text(text = "Continue")
                }

            }
        }
    }


}

