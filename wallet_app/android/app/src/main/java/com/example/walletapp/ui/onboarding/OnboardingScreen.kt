package com.example.walletapp.ui.onboarding

.border

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.walletapp.R
import com.example.walletapp.ui.Onboarding
import com.example.walletapp.ui.components.StarknetLogo
import com.example.walletapp.ui.components.TransparentButton
import com.example.walletapp.ui.theme.WalletappTheme


@Composable
fun OnboardingScreen (
    onNavigateCreateAccount: () -> Unit,
    onNavigateImportAccount: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color("#0C0C4F".toColorInt()))
            .padding(20.dp)
    ) {
        Text(
            text = "Starknet Wallet",
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 28.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 70.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        StarknetLogo(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Adjust space between buttons
        ) {
            TransparentButton(
                label = "Create a New Account",
                onClick = onNavigateCreateAccount
            )

            Spacer(modifier = Modifier.height(10.dp))

            TransparentButton(
                label = "Import Existing Account",
                onClick = onNavigateImportAccount
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
@Preview 
fun PreviewOnboardingScreen() {
    WalletappTheme {
        Surface {
           OnboardingScreen(
               onNavigateCreateAccount = { println("create new account pressed") },
               onNavigateImportAccount = { println("import existing account pressed")}
           )
        }
    }
}

