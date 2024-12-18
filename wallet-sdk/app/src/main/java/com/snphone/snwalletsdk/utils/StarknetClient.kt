package com.snphone.snwalletsdk.utils

import android.util.Log
import com.swmansion.starknet.account.Account
import com.swmansion.starknet.account.StandardAccount
import com.swmansion.starknet.crypto.StarknetCurve
import com.swmansion.starknet.data.ContractAddressCalculator
import com.swmansion.starknet.data.types.CairoVersion
import com.swmansion.starknet.data.types.Call
import com.swmansion.starknet.data.types.DeployAccountResponse
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.data.types.StarknetChainId
import com.swmansion.starknet.data.types.Uint256
import com.swmansion.starknet.extensions.toFelt
import com.swmansion.starknet.extensions.toNumAsHex
import com.swmansion.starknet.provider.rpc.JsonRpcProvider
import com.swmansion.starknet.signer.StarkCurveSigner
import kotlinx.coroutines.future.await
import java.math.BigInteger
import java.security.SecureRandom


const val ETH_ERC20_ADDRESS = "0x03f58b3b48d59f6ce07d27e2e62d10cbd31ce966fc285d817674b97272ae8db9"
const val ACCOUNT_CLASS_HASH = "0x061dac032f228abef9c6626f995015233097ae253a7f72d68552db02f2971b8f"

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

    suspend fun test(){
        val request = provider.getBlockWithTxs(1)
        val response = request.send()
        Log.d(tag, "test: ${response.toString()}")
    }

    suspend fun getBalance(accountAddress: Felt,contractAddress:Felt): Uint256 {

        // Create a call to Starknet ERC-20 ETH contract
        val call = Call(
            contractAddress = contractAddress,
            entrypoint = "balanceOf", // entrypoint can be passed both as a string name and Felt value
            calldata = listOf(accountAddress), // calldata is List<Felt>, so we wrap accountAddress in listOf()
        )

        // Create a Request object which has to be executed in synchronous or asynchronous way
        val request = provider.callContract(call)

        // Execute a Request. This operation returns JVM CompletableFuture
        val future = request.sendAsync()

        // Await the completion of the future without blocking the main thread
        // this comes from kotlinx-coroutines-jdk8
        // The result of the future is a List<Felt> which represents the output values of the balanceOf function
        val response = future.await()

        // Output value's type is UInt256 and is represented by two Felt values
        return Uint256(
            low = response[0],
            high = response[1],
        )
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
}
