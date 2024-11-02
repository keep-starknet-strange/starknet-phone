package com.example.walletapp.utils

import com.swmansion.starknet.data.types.Uint256
import java.math.BigDecimal
import java.text.DecimalFormat

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
