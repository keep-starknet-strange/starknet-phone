package com.example.beerus_android

import com.example.beerus_android.Beerus;

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import com.example.beerus_android.ui.theme.BeerusandroidTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            BeerusandroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Toggle(modifier=Modifier.padding(20.dp))
                }
            }
        }
    }
}

@Composable
fun Toggle(modifier: Modifier = Modifier) {
    var runState by rememberSaveable {
        mutableStateOf("Not Running")
    }
    var checked by rememberSaveable { mutableStateOf(false)}

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color("#0C0C4F".toColorInt()))
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Spacer(modifier=Modifier.height(40.dp))

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            },
            thumbContent = if (checked) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                null
            }
        )
        Spacer(modifier=Modifier.height(40.dp))

        if(checked){
            startBeerus()
            runState="Running"
        }else{
            runState="Not Running"
        }

        Text(
            text = runState,
            fontFamily = FontFamily(Font(R.font.publicsans_regular)),
            color = Color.White,
            fontSize = 14.sp
        )

    }


}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    BeerusandroidTheme {
//        Greeting("Android")
//    }
//}

fun startBeerus() {
    Beerus.run(
        "",
        "",
        "tmp",
        5,
        8080
    )
}