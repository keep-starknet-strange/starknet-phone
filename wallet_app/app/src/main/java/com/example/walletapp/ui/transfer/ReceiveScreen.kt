package com.example.walletapp.ui.transfer

import android.content.ClipData
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.walletapp.R

@Composable
fun ReceiveScreen(modifier: Modifier) {
    val clipboard: ClipboardManager = LocalClipboardManager.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color("#0C0C4F".toColorInt())) // Your dark blue background color
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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

            // Wallet Address
            Text(
                text = "0xfoo...123", // Replace with actual wallet address
                fontFamily = FontFamily(Font(R.font.publicsans_bold)),
                color = Color.White,
                fontSize = 40.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Clickable Text for Copy
            Text(
                text = "Click to copy address",
                fontFamily = FontFamily(Font(R.font.publicsans_regular)),
                color = Color.White,
                fontSize = 15.sp,
                modifier = Modifier.clickable {
                    val clip = ClipEntry(ClipData.newPlainText("Wallet Address", "0xfoo...123")) // TODO: Replace with actual wallet address
                    clipboard.setClip(clip)
                }
            )
        }
    }

}