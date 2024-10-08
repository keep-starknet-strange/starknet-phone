package com.example.walletapp.ui.activity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxWidth
import com.example.walletapp.ui.theme.WalletappTheme

class RecoveryPhraseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            WalletappTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    RecoveryPhraseScreenView(
                        modifier = Modifier.padding(10.dp),
                        navController = navController
                    )
                }
            }
        }
    }

    @Composable
    fun RecoveryPhraseScreenView(modifier: Modifier, navController: NavController) {
        var isVisible by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color("#0C0C4F".toColorInt()))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Image(
                        painter = painterResource(R.drawable.arrow_back_ios),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "Secret Recovery Seed Phrase",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    modifier = Modifier.weight(2f),
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Secure it now! This is your only way to recover your wallet if your Starknet wallet is deleted, or if your device is lost, stolen, or becomes unusable.",
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(40.dp))

            if (isVisible) SeedPhrases() else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.warning_sign),
                        contentDescription = "warning",
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = "Hope no one is looking at your screen",
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { isVisible = !isVisible },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt())),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp)
                ) {
                    Image(
                        painter = painterResource(if (isVisible) R.drawable.visibility_off else R.drawable.visibility),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = if (isVisible) "Hide" else "Show",
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = { /* Handle copy action */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt())),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.copy),
                        contentDescription = "copy",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = "Copy",
                        fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.progress_2),
                contentDescription = "progress",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { showDialog = !showDialog  },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color("#1B1B76".toColorInt())),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp)
            ) {
                Text(text = "Next", color = Color.White, fontSize = 17.sp)
            }

            if (showDialog) {
                CustomDialog(onDismiss = { showDialog = false })
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }

    @Composable
    fun SeedPhrases() {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(generateSeedWords()) { item ->
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color("#1B1B76".toColorInt()), shape = CircleShape)
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = "${item.number}",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = item.text, fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

data class SeedWordItem(val number: Int, val text: String)

fun generateSeedWords(): List<SeedWordItem> {
    val randomWords = listOf(
        "apple", "banana", "cherry", "date", "elderberry", "fig", "grape", "honeydew",
        "kiwi", "lemon", "mango", "nectarine", "orange", "papaya", "quince", "raspberry",
        "strawberry", "tangerine", "ugli", "vanilla", "watermelon", "xigua", "yellow", "zucchini"
    )
    return List(12) { SeedWordItem(it + 1, randomWords.random()) }
}

@Composable
fun CustomDialog(onDismiss: () -> Unit) {
    val context = (LocalContext.current as Activity)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.check_symbol),
//                    contentDescription = "success",
//                    modifier = Modifier
//                        .size(80.dp)
//                        .background(Color.White, shape = CircleShape)
//                        .padding(8.dp)
//                        .align(Alignment.CenterHorizontally)
//                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Congratulations!",
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Text(
                text = "You’re done creating your wallet",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = { val i = Intent(context, WalletActivity::class.java)
                    context.startActivity(i) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color("#2C74DD".toColorInt()))
            ) {
                Text(
                    text = "Let’s start!",
                    color = Color.White
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color("#0C0C4F".toColorInt()),
        contentColor = Color.White,
        modifier = Modifier
            .width(500.dp)
            .padding(8.dp)
    )
}