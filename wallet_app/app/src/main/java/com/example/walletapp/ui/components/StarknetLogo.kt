package com.example.walletapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.walletapp.R
import com.example.walletapp.ui.theme.WalletappTheme

@Composable
fun StarknetLogo (modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.starknet_icon),
        contentDescription = "Starknet Logo",
        modifier = modifier.size(123.dp) )
}

@Composable
@Preview
fun PreviewStarknetLogo() {
    WalletappTheme {
        StarknetLogo()
    }
}
