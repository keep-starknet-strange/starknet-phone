package com.example.walletapp.utils

import java.math.BigDecimal
import java.text.DecimalFormat

// Function to format BigDecimal to Double with 2 decimal places
fun BigDecimal.toDoubleWithTwoDecimal(): String {
    val decimalFormat = DecimalFormat("#.00")
    return decimalFormat.format(this.toDouble())
}
fun Double.toDoubleWithTwoDecimal(): String {
    val decimalFormat = DecimalFormat("#.00")
    val formattedValue = decimalFormat.format(this)
    return if (this < 1) "0$formattedValue" else formattedValue
}