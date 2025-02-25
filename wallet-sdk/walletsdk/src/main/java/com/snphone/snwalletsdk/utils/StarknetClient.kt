package com.snphone.snwalletsdk.utils

import android.content.Context
import android.util.Log
import com.swmansion.starknet.account.Account
import com.swmansion.starknet.account.StandardAccount
import com.swmansion.starknet.crypto.StarknetCurve
import com.swmansion.starknet.data.ContractAddressCalculator
import com.swmansion.starknet.data.types.Call
import com.swmansion.starknet.data.types.DeployAccountResponse
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.data.types.StarknetChainId
import com.swmansion.starknet.data.types.Uint256
import com.swmansion.starknet.extensions.toFelt
import com.swmansion.starknet.provider.rpc.JsonRpcProvider
import com.swmansion.starknet.signer.StarkCurveSigner
import kotlinx.coroutines.future.await
import java.math.BigDecimal
import java.math.BigInteger
import com.snphone.snwalletsdk.utils.Signer

class StarknetClient(private val context: Context, private val rpcUrl: String) {

     private val ETH_ERC20_ADDRESS = "0x04718f5a0fc34cc1af16a1cdee98ffb20c31f5cd61d6ab07201858f4287c938d"
     private val ACCOUNTCLASSHASH = "0x061dac032f228abef9c6626f995015233097ae253a7f72d68552db02f2971b8f"

    private val provider = JsonRpcProvider(rpcUrl)
    private val tag = "StarknetClient"

    fun deployAccount(): Pair<String, String> {
        // Predefined values for account creation

        val privateKey = Signer(context).generatePrivateKey()
        val publicKey = StarknetCurve.getPublicKey(privateKey)

        Log.d(tag, "Private key: ${privateKey.hexString()}")
        Log.d(tag,"Public key: ${publicKey.hexString()}")

        val signer = StarkCurveSigner(privateKey)

        val salt = Felt.ONE

        val calldata = listOf(publicKey)

        val accountContractClassHash = Felt.fromHex(ACCOUNTCLASSHASH)

        val address = ContractAddressCalculator.calculateAddressFromHash(
            classHash = accountContractClassHash,
            calldata = calldata,
            salt = salt
        )

        val account = StandardAccount(
            address = address,
            signer = signer,
            provider = provider,
            chainId = StarknetChainId.SEPOLIA,
        )


        // Fund address first with Eth
        // how to approach this, we need the user to fund the address with eth

        val payload = account.signDeployAccountV1(
            classHash = accountContractClassHash,
            calldata = listOf(publicKey),
            salt = salt,
            // 10*fee from estimate deploy account fee
            maxFee = Felt.fromHex("0x219d2e16ea"),
        )

        val res: DeployAccountResponse  = provider.deployAccount(payload).send()
        Log.d(tag, "Account deployed successfully: $res")

        val deployedAddress = res.address?.hexString() ?: ""
        return Pair(privateKey.hexString(), deployedAddress)
    }

    suspend fun getBalance(accountAddress: Felt): String {
        // Create a call to Starknet ERC-20 ETH contract
        val call = Call(
            contractAddress = ETH_ERC20_ADDRESS.toFelt,
            entrypoint = "balanceOf",
            calldata = listOf(accountAddress), // calldata is List<Felt>
        )

        // Create a Request object which has to be executed in synchronous or asynchronous way
        val request = provider.callContract(call)

        // Execute a Request asynchronously and handle potential errors
        return try {
            val future = request.sendAsync()
            val response = future.await() // Await the completion without blocking

            // Validate response size
            require(response.size >= 2) { "Response size is less than 2; cannot construct Uint256" }

            // Output value's type is UInt256 and is represented by two Felt values
            val balance = Uint256(
                low = response[0],
                high = response[1],
            )

            convertToTokenBalance(balance)
        } catch (e: Exception) {
            // Handle or log the exception as needed
            throw RuntimeException("Failed to fetch balance $e", e)
        }
    }


    suspend fun transferFunds(account: Account, toAddress: Felt, amount: Uint256): Felt {
        val erc20ContractAddress = Felt.fromHex(ETH_ERC20_ADDRESS)
        val calldata = listOf(toAddress) + amount.toCalldata()
        val call = Call(
            contractAddress = erc20ContractAddress,
            entrypoint = "transfer",
            calldata = calldata,
        )
        val request = account.executeV3(call)
        val future = request.sendAsync()
        val response =  future.await()
        return response.transactionHash
    }

    private fun convertToTokenBalance(balance: Uint256): String {
        // Convert the Uint256 to BigInteger for easier manipulation
        val bigIntBalance = balance.value

        // Use fixed divisor for 18 decimals (10^18)
        val divisor = BigInteger.TEN.pow(18)

        // Divide the balance by the divisor to get the whole number part
        val whole = bigIntBalance.divide(divisor)

        // Get the fractional part by taking the remainder
        val fractional = bigIntBalance.remainder(divisor)

        // Convert fractional to string and pad with leading zeros if necessary
        var fractionalStr = fractional.toString()
        // Pad with leading zeros to match 18 decimal places
        while (fractionalStr.length < 18) {
            fractionalStr = "0$fractionalStr"
        }

        // Trim trailing zeros from fractional part
        fractionalStr = fractionalStr.trimEnd('0')

        // Construct the final string
        return if (fractionalStr.isEmpty()) {
            whole.toString()
        } else {
            "$whole.$fractionalStr"
        }
    }

    companion object {
        fun toUint256Amount(amount: String, decimals: Int = 18): Uint256 {
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
}
