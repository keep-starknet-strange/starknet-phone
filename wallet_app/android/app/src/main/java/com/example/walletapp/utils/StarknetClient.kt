package com.example.walletapp.utils

import com.example.walletapp.BuildConfig
import com.swmansion.starknet.account.StandardAccount
import com.swmansion.starknet.data.types.Call
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.data.types.Uint256
import com.swmansion.starknet.provider.rpc.JsonRpcProvider
import com.swmansion.starknet.signer.StarkCurveSigner
import kotlinx.coroutines.future.await
import java.math.BigDecimal

const val ETH_ERC20_ADDRESS = "0x049d36570d4e46f48e99674bd3fcc84644ddd6b96f7c741b1562b82f9e004dc7"

class StarknetClient(private val rpcUrl: String) {

    private val provider = JsonRpcProvider(rpcUrl)
    private val privateKey= BuildConfig.PRIVATE_KEY
    private val accountAddress= BuildConfig.ACCOUNT_ADDRESS

    suspend fun deployAccount() {


        // Predefined values for account creation
        val privateKey = Felt.fromHex(privateKey)
        val accountAddress = Felt.fromHex(accountAddress)

        val signer = StarkCurveSigner(privateKey)
        val chainId = provider.getChainId().sendAsync().await()
        val account = StandardAccount(
            address = accountAddress,
            signer = signer,
            provider = provider,
            chainId = chainId,
            cairoVersion = Felt.ONE,
        )

        // TODO: deploy account
    }

    suspend fun getEthBalance(accountAddress: Felt): Uint256 {
        val erc20ContractAddress = Felt.fromHex(ETH_ERC20_ADDRESS)

        // Create a call to Starknet ERC-20 ETH contract
        val call = Call(
            contractAddress = erc20ContractAddress,
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

    suspend fun sendERC20() {
        // TODO(#24)
    }

    fun weiToEther(wei: Uint256): BigDecimal {
        val weiInEther = BigDecimal("1000000000000000000") // 10^18
        return BigDecimal(wei.value.toString()).divide(weiInEther)
    }
}
