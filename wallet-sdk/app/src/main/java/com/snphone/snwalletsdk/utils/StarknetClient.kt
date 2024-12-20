package com.snphone.snwalletsdk.utils

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
import java.math.BigInteger


const val ETH_ERC20_ADDRESS = "0x03f58b3b48d59f6ce07d27e2e62d10cbd31ce966fc285d817674b97272ae8db9"
const val ACCOUNT_CLASS_HASH = "0x061dac032f228abef9c6626f995015233097ae253a7f72d68552db02f2971b8f"
const val CONTRACT_ADDRESS = "0x04718f5a0fc34cc1af16a1cdee98ffb20c31f5cd61d6ab07201858f4287c938d"

class StarknetClient(rpcUrl: String) {

    private val provider = JsonRpcProvider(rpcUrl)
    private val tag = "StarknetClient"
    private val keystore = Keystore()

    suspend fun deployAccount(): String {
        // Predefined values for account creation
        val randomPrivateKey = StandardAccount.generatePrivateKey()
        keystore.storeData(randomPrivateKey.value.toString())       // save the key generated
        val data = keystore.retrieveData()                          // retrieve it to generate public key
        val privateKey = BigInteger(data).toFelt

        val publicKey = StarknetCurve.getPublicKey(privateKey)

        Log.d(tag, "Private key: ${privateKey.hexString()}")
        Log.d(tag,"Public key: ${publicKey.hexString()}")

        val signer = StarkCurveSigner(privateKey)

        val salt = Felt.ONE

        val calldata = listOf(publicKey)

        val accountContractClassHash = Felt.fromHex(ACCOUNT_CLASS_HASH)

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

//        val payloadForFeeEstimation = account.signDeployAccountV1(
//            classHash = accountContractClassHash,
//            calldata = calldata,
//            salt = salt,
//            maxFee = Felt.ZERO,
//            nonce = Felt.ZERO,
//            forFeeEstimate = true,
//        )
//        val feePayload = provider.getEstimateFee(listOf(payloadForFeeEstimation)).send()

        // Fund address first
        // how to approach this, we need the user to fund the address

        val payload = account.signDeployAccountV1(
            classHash = accountContractClassHash,
            calldata = listOf(publicKey),
            salt = salt,
            // 10*fee from estimate deploy account fee
            maxFee = Felt.fromHex("0x219d2e16ea"),
        )

        val res: DeployAccountResponse  = provider.deployAccount(payload).send()
        Log.d(tag, "Account deployed successfully: $res")

        return res.address?.hexString() ?: ""

    }

    suspend fun getBalance(accountAddress: Felt): String {
        // Create a call to Starknet ERC-20 ETH contract
        val call = Call(
            contractAddress = CONTRACT_ADDRESS.toFelt,
            entrypoint = "balanceOf",
            calldata = listOf(accountAddress), // calldata is List<Felt>
        )

        // Create a Request object which has to be executed in synchronous or asynchronous way
        val request = provider.callContract(call)

        // Execute a Request asynchronously and handle potential errors
        return try {
            val future = request.sendAsync()
            val response = future.await() // Await the completion without blocking

            Log.d(tag, "Response: $response")

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
            throw RuntimeException("Failed to fetch balance", e)
        }
    }


    suspend fun transferFunds(account: Account, toAddress: Felt, amount: Uint256): Felt {
        //TODO: Add support to starknet
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
}
