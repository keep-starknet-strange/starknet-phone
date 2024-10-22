package com.example.walletapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walletapp.R
import com.example.walletapp.ui.theme.WalletappTheme
import com.example.walletapp.ui.theme.WhiteFlare

@Composable
fun TransparentButton(
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, WhiteFlare),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = label,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            color = Color.White,
            fontSize = 17.sp
        )
    }
}

@Preview
@Composable
fun PreviewTransparentButton() {
    WalletappTheme {
        Surface {
            TransparentButton(label = "Button Label", onClick = {})
        }
    }
}