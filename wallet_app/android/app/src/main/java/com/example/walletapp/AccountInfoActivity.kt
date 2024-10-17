package com.example.walletapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.walletapp.CreatePinActivity
import com.example.walletapp.R
import com.example.walletapp.ui.theme.WalletappTheme
import org.bouncycastle.math.raw.Mod

class AccountInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WalletappTheme {
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
                                        text = "Create Account",
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
                            .padding(top = 50.dp, start = 16.dp, end = 16.dp )

                    ) {

                        Spacer(modifier = Modifier.height(5.dp))
                        AccountInfoView()


                    }
                }
            }

        }
    }
}

@Composable
fun AccountInfoView() {

    val context = (LocalContext.current as Activity)

    var checked by remember { mutableStateOf(true) }


    Column (
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)

    ) {

    Text(text=  "Account Information", style = TextStyle(color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp))
        Spacer(modifier = Modifier.height(10.dp))
        Card (
            backgroundColor = Color("#141462".toColorInt()),
            shape = RoundedCornerShape(size = 8.dp),
            modifier = Modifier.fillMaxWidth()
                .background(color = Color("#141462".toColorInt()))
                .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 15.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(){
                        Column {
                            Text(text=  "Account name", style = TextStyle(color = Color("#8D8DBB".toColorInt()), fontWeight = FontWeight.Medium, fontSize = 15.sp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text=  "Starknet", style = TextStyle(color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp))

                        }
                    }

                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .background(color = Color.Transparent)
                            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(4.dp))
                            .padding(4.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.content_copy),
                            contentDescription = "Copy",
                            modifier = Modifier.size(20.dp)
                        )
                    }


                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(){
                        Column {
                            Text(text=  "Private key", style = TextStyle(color = Color("#8D8DBB".toColorInt()), fontWeight = FontWeight.Medium, fontSize = 15.sp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text=  "q78ggh277ibckewjtnM", style = TextStyle(color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp))

                        }
                    }


                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .background(color = Color.Transparent)
                            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(4.dp))
                            .padding(4.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.content_copy),
                            contentDescription = "Copy",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }



                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt()), contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
                .background(color = Color("#141462".toColorInt()))
                .height(51.dp)
                .border(width = 1.dp, color = Color("#1B1B76".toColorInt()), shape = RoundedCornerShape(8.dp))

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                 verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center


            ){

                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Backward  Arrow",
                    modifier = Modifier.padding(start = 8.dp),
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = "Generate new private key", style = TextStyle(color = Color("#A3A3C1".toColorInt()),  fontSize = 16.sp, fontWeight = FontWeight.Medium))
            }
        }


        Spacer(modifier = Modifier.height(8.dp))



        Button(
            onClick = { val i = Intent(context, ImportExistingKeyActivity::class.java)
                context.startActivity(i) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt()), contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
                .background(color = Color("#1B1B76".toColorInt()))
                .height(51.dp)
                .border(width = 1.dp, color = Color("#CBC5C5".toColorInt()), shape = RoundedCornerShape(8.dp))


        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){

                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Backward  Arrow",
                    modifier = Modifier.padding(start = 8.dp),
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = "Use existing private key", style = TextStyle(color = Color.White, fontSize = 16.sp,
                    fontWeight = FontWeight.Medium))
            }
        }

        Spacer(modifier = Modifier.height(13.dp))

        Box(
            modifier = Modifier
                .background(color = Color("#221451".toColorInt(), ))
                .height(74.dp)
                .border(width = 1.dp, color = Color("#221451".toColorInt()), shape = RoundedCornerShape(16.dp))
                .padding(16.dp)

        ){
            Row(){


                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Info",
                    tint = Color("#E45F61".toColorInt())
                )


                Spacer(modifier = Modifier.width(10.dp))



                Text(
                    text = "Store your private key securely. In case of its lost or stolen, access to funds will be lost without the possibility of recovery",
                    style = TextStyle(color = Color("#E45F61".toColorInt()), fontSize = 14.sp)
                )
            }
        }


        Spacer(modifier = Modifier.height(20.dp))



        Row(
            verticalAlignment = Alignment.CenterVertically,

        ) {

            Checkbox(
                modifier = Modifier.border(width = 1.dp, color = Color("#54DC84".toColorInt()), shape = RoundedCornerShape(30.dp))
                    .height(25.dp)
                    .width(25.dp),
                checked = checked,
                onCheckedChange = { checked = it },
                colors = CheckboxDefaults.colors(checkedColor = Color.Transparent, checkmarkColor = Color("#54DC84".toColorInt())),

            )


            Spacer(modifier = Modifier.width(10.dp))
            Text(
                "I have read the information and saved the private key",
                style = TextStyle(color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
            )
        }


        Spacer(modifier = Modifier.weight(1f))


        Button(
            onClick = { val i = Intent(context, CreatePinActivity::class.java)
                context.startActivity(i) },
            contentPadding = ButtonDefaults.ContentPadding,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color("#EC796B".toColorInt()), contentColor = Color.White),
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


        Spacer(modifier = Modifier.height(20.dp))


    }

}







