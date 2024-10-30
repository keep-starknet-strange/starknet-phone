package com.example.walletapp.utils

import com.example.walletapp.BuildConfig
import com.swmansion.starknet.account.StandardAccount
import com.swmansion.starknet.data.types.Call
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.data.types.Uint256
import com.swmansion.starknet.provider.rpc.JsonRpcProvider
import com.swmansion.starknet.signer.StarkCurveSigner
import com.swmansion.starknet.account.Account
import kotlinx.coroutines.future.await

import java.math.BigDecimal
import android.util.Log
import com.swmansion.starknet.data.types.CairoVersion
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import android.content.Context
import com.swmansion.starknet.crypto.StarknetCurve
import com.swmansion.starknet.extensions.toFelt
import com.swmansion.starknet.extensions.toNumAsHex
import java.io.IOException
import java.math.BigInteger
import java.security.GeneralSecurityException

class StarknetClient(private val rpcUrl: String) {

    private val provider = JsonRpcProvider(rpcUrl)
    private val privateKey = BuildConfig.PRIVATE_KEY
    private val accountAddress = BuildConfig.ACCOUNT_ADDRESS
    private val tag = "StarknetClient"
    private val keystore = Keystore()

    suspend fun deployAccount() {
        // Predefined values for account creation

        val randomPrivateKey = StandardAccount.generatePrivateKey()
        keystore.storeData(randomPrivateKey.value.toString())       // save the key generated
        val data = keystore.retrieveData()                          // retrieve it to generate public key
        val privateKey = BigInteger(data).toFelt

        val accountAddress = StarknetCurve.getPublicKey(privateKey)

        val signer = StarkCurveSigner(privateKey)
        val chainId = provider.getChainId().sendAsync().await()
        val account = StandardAccount(
            address = accountAddress,
            signer = signer,
            provider = provider,
            chainId = chainId,
            cairoVersion = CairoVersion.ONE,
        )

        // TODO(#99): add account deployment logic

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

    suspend fun transferFunds(account: Account, toAddress: Felt, amount: Uint256) {
        // TODO(#102): add logic to transfer funds here
        // follow the example: https://github.com/software-mansion/starknet-jvm/blob/main/androiddemo/src/main/java/com/example/androiddemo/MainActivity.kt
    }



}
