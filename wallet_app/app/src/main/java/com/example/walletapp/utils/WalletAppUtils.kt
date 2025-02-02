package com.example.walletapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.swmansion.starknet.data.types.Uint256
import java.math.BigDecimal
import java.security.MessageDigest
import java.text.DecimalFormat
import java.util.Base64

// Function to format BigDecimal to Double with 2 decimal places
fun BigDecimal.toDoubleWithTwoDecimal(): Double {
    val decimalFormat = DecimalFormat("#.00")
    return decimalFormat.format(this.toDouble()).toDouble()
}
fun Double.toDoubleWithTwoDecimal(): Double {
    val decimalFormat = DecimalFormat("#.00")
    val formattedValue = decimalFormat.format(this)
    return if (this < 1) "0$formattedValue".toDouble() else formattedValue.toDouble()
}

fun weiToEther(wei: Uint256): BigDecimal {
    val weiInEther = BigDecimal("1000000000000000000") // 10^18
    return BigDecimal(wei.value.toString()).divide(weiInEther)
}

fun etherToWei(ether: BigDecimal): Uint256 {
    val weiInEther = BigDecimal("1000000000000000000") // 10^18
    val weiValue = ether.multiply(weiInEther).toBigInteger()
    return Uint256(weiValue)
}

fun isValidEthereumAddress(address: String): Boolean {
    val regex = Regex("^0x[a-fA-F0-9]+\$")
    return regex.matches(address)
}

@RequiresApi(Build.VERSION_CODES.O)
fun hashPin(pin: String): String {
    // Create a MessageDigest instance for SHA-256
    val digest = MessageDigest.getInstance("SHA-256")

    val encodedhash = digest.digest(pin.toByteArray())

    val hexString = StringBuilder()
    for (b in encodedhash) {
        val hex = String.format("%02x", b)
        hexString.append(hex)
    }
    return hexString.toString()
}


