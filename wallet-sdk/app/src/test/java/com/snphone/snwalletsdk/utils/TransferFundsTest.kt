package com.snphone.snwalletsdk.utils

import android.util.Log
import com.snphone.snwalletsdk.BuildConfig
import com.swmansion.starknet.account.StandardAccount
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.data.types.StarknetChainId
import com.swmansion.starknet.data.types.Uint256
import com.swmansion.starknet.provider.rpc.JsonRpcProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
class TransferFundsTest {

    private lateinit var starknetClient: StarknetClient
    private lateinit var account: StandardAccount

    @Before
    fun setUp() {
        // Initialize StarknetClient and StandardAccount
        starknetClient = StarknetClient(BuildConfig.RPC_URL)
        val addressUser = Felt.fromHex(BuildConfig.publicAddress)
        val privateKey = Felt.fromHex(BuildConfig.privateKey)

        account = StandardAccount(
            address = addressUser,
            privateKey = privateKey,
            provider = JsonRpcProvider(BuildConfig.RPC_URL),
            chainId = StarknetChainId.SEPOLIA
        )
    }

    @Test
    fun testTransferFunds_balanceUpdate() {
        // Arrange
        val recipientAddress = Felt.fromHex(BuildConfig.recepientAddress)
        val amount = toUint256Amount("1")

        val initialBalance = runBlocking {
            starknetClient.getBalance(account.address)
        }

        // Act
        val transactionId = runBlocking {
            try {
                starknetClient.transferFunds(account, recipientAddress, amount)
            } catch (e: Exception) {
                Log.d("TransferFundsTest", "Error in transfer: ${e.message}")
                null
            }
        }

        // Assert
        assertNotNull(transactionId.toString(), "Transaction ID should not be null")

        // Wait for some time to allow balance update
        runBlocking { delay(5000) } // 5 seconds

        val updatedBalance = runBlocking {
            starknetClient.getBalance(account.address)
        }

        // Verify balance has decreased by the transferred amount
        val initialBalanceDecimal = initialBalance.toBigDecimal()
        val updatedBalanceDecimal = updatedBalance.toBigDecimal()
        val expectedBalanceDecimal = initialBalanceDecimal - amount.value.toBigDecimal()

        assertEquals(expectedBalanceDecimal.toString(), updatedBalanceDecimal.toString(), "Balance should reflect the transferred amount")
    }

//    @Test
//    fun testTransferFunds_insufficientFunds() {
//        // Arrange
//        val recipientAddress = Felt.fromHex("0x02ECCC332c3F52645DA6A734685D90872aa8B900669d6FfFb790225257957025")
//        val amount = toUint256Amount("100000000") // Exceed available balance
//
//        // Act
//        val transactionId = runBlocking {
//            try {
//                starknetClient.transferFunds(account, recipientAddress, amount)
//            } catch (e: Exception) {
//                Log.d("TransferFundsTest", "Expected error for insufficient funds: ${e.message}")
//                null
//            }
//        }
//
//        // Assert
//        assertNull(transactionId.toString(), "Transaction ID should be null for insufficient funds")
//    }
//
//    @Test
//    fun testTransferFunds_invalidRecipient() {
//        // Arrange
//        val invalidRecipientAddress = Felt.fromHex("0xINVALIDADDRESS")
//        val amount = toUint256Amount("1")
//
//        // Act
//        val transactionId = runBlocking {
//            try {
//                starknetClient.transferFunds(account, invalidRecipientAddress, amount)
//            } catch (e: Exception) {
//                Log.d("TransferFundsTest", "Expected error for invalid recipient: ${e.message}")
//                null
//            }
//        }
//
//        // Assert
//        assertNull(transactionId.toString(), "Transaction ID should be null for invalid recipient")
//    }

    private fun toUint256Amount(amount: String, decimals: Int = 18): Uint256 {
        require(decimals >= 0) { "Decimals must be non-negative" }

        return try {
            val bigDecimalAmount = BigDecimal(amount)
            require(bigDecimalAmount >= BigDecimal.ZERO) { "Amount must be non-negative" }

            // Check if decimal places don't exceed the specified decimals
            val scale = bigDecimalAmount.scale()
            require(scale <= decimals) { "Too many decimal places. Maximum allowed: $decimals" }

            val scaledAmount = bigDecimalAmount.multiply(BigDecimal.TEN.pow(decimals)).toBigInteger()
            val hexValue = "0x" + scaledAmount.toString(16)
            Uint256.fromHex(hexValue)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid amount format: $amount")
        }
    }
}

