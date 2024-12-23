package com.example.walletapp.utils

import com.example.walletapp.BuildConfig
import com.swmansion.starknet.account.StandardAccount
import com.swmansion.starknet.data.ContractAddressCalculator
import com.swmansion.starknet.data.types.Call
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.data.types.Uint256
import com.swmansion.starknet.provider.rpc.JsonRpcProvider
import com.swmansion.starknet.signer.StarkCurveSigner
import com.swmansion.starknet.account.Account
import kotlinx.coroutines.future.await
import java.security.SecureRandom

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


const val ETH_ERC20_ADDRESS = "0x049d36570d4e46f48e99674bd3fcc84644ddd6b96f7c741b1562b82f9e004dc7"
const val ACCOUNT_CLASS_HASH = "0x04c6d6cf894f8bc96bb9c525e6853e5483177841f7388f74a46cfda6f028c755"

class StarknetClient(private val rpcUrl: String) {

    private val provider = JsonRpcProvider(rpcUrl)
    private val keystore = Keystore()

    suspend fun deployAccount() {
        // Predefined values for account creation
        val randomPrivateKey = StandardAccount.generatePrivateKey()
        keystore.storeData(randomPrivateKey.value.toString())       // save the key generated
        val data = keystore.retrieveData()                          // retrieve it to generate public key
        val privateKey = BigInteger(data).toFelt

        val publicKey = StarknetCurve.getPublicKey(privateKey)

        val signer = StarkCurveSigner(privateKey)

        val chainId = provider.getChainId().sendAsync().await()

        val salt = BigInteger(250, SecureRandom()).toFelt

        val accountContractClassHash = Felt.fromHex(ACCOUNT_CLASS_HASH)

        val address = ContractAddressCalculator.calculateAddressFromHash(
            classHash = accountContractClassHash,
            calldata = listOf(publicKey),
            salt = salt,
        )

        val account = StandardAccount(
            address = address,
            signer = signer,
            provider = provider,
            chainId = chainId,
            cairoVersion = CairoVersion.ONE,
        )

        // Fund address first
        // how to approach this, we need the user to fund the address

        val payload = account.signDeployAccountV1(
            classHash = accountContractClassHash,
            calldata = listOf(publicKey),
            salt = salt,
            // 10*fee from estimate deploy account fee
            maxFee = Felt.fromHex("0x11fcc58c7f7000"),
        )
        provider.deployAccount(payload).send()
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
