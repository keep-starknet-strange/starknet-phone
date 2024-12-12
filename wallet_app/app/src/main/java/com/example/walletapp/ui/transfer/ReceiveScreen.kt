package com.example.walletapp.ui.transfer

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.walletapp.BuildConfig
import com.example.walletapp.R

@Composable
fun ReceiveScreen(modifier: Modifier,navController: NavController) {
    val clipboard: ClipboardManager = LocalClipboardManager.current
    val context= LocalContext.current
    val address= BuildConfig.ACCOUNT_ADDRESS

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color("#0C0C4F".toColorInt())) // Your dark blue background color
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
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
            Spacer(modifier = Modifier.height(40.dp))
            // QR Code Image
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .background(Color.White) // White background for QR code
            ) {
                Image(
                    painter = painterResource(id = R.drawable.scanner), // Replace with actual QR code resource
                    contentDescription = "QR Code",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(
                    text = address.take(8) + "...",
                    fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                    color = Color.White,
                    fontSize = 30.sp
                )
                Icon(
                    painter = painterResource(R.drawable.copy),// replace with your Ethereum icon
                    contentDescription = null,
                    tint=Color.White,
                    modifier = Modifier.padding(start=5.dp)
                        .size(25.dp).clickable {
                            val clip = ClipEntry(ClipData.newPlainText("Wallet Address", address))
                            clipboard.setClip(clip)
                            Toast.makeText(context, "Address Copied", Toast.LENGTH_LONG).show()
                        }
                )

            }
        }
    }

}