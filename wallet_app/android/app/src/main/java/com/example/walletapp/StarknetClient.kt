import com.example.walletapp.BuildConfig
import com.swmansion.starknet.account.StandardAccount
import com.swmansion.starknet.data.types.Call
import com.swmansion.starknet.data.types.Felt
import com.swmansion.starknet.data.types.Uint256
import com.swmansion.starknet.provider.rpc.JsonRpcProvider
import com.swmansion.starknet.signer.StarkCurveSigner
import kotlinx.coroutines.future.await
import java.math.BigDecimal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.spec.ECGenParameterSpec
import kotlin.math.log

const val ETH_ERC20_ADDRESS = "0x049d36570d4e46f48e99674bd3fcc84644ddd6b96f7c741b1562b82f9e004dc7"

class StarknetClient(private val rpcUrl: String) {

    private val provider = JsonRpcProvider(rpcUrl)
    private val privateKey = BuildConfig.PRIVATE_KEY
    private val accountAddress = BuildConfig.ACCOUNT_ADDRESS

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

    fun test() {
        val KEY_ALIAS = "your_web3_key_alias"
        val ANDROID_KEYSTORE = "AndroidKeyStore"
        val keyPair = CryptoUtils.generateWeb3KeyPair()
        if (keyPair != null) {
            val privateKey = keyPair.private
            val publicKey = keyPair.public
            Log.d("STK", "private key: ${Felt.fromHex("0x123")}")
            Log.d("STK", "public key: $publicKey")

            // Use the privateKey and publicKey for your wallet operations
        } else {
            // Key pair already exists or an error occurred
            Log.e("STK", "key already exist")
            Log.d("STK", "private key: ${Felt.fromHex("0x123")}")

        }

        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }
        val privateKeyEntry = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.PrivateKeyEntry
            ?: throw IllegalStateException("Key not found")

        val publicKey = privateKeyEntry.certificate.publicKey

        Log.d("STK", "access key: ${publicKey}")

    }

    fun weiToEther(wei: Uint256): BigDecimal {
        val weiInEther = BigDecimal("1000000000000000000") // 10^18
        return BigDecimal(wei.value.toString()).divide(weiInEther)
    }

    object CryptoUtils {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "your_web3_key_alias"

        fun generateWeb3KeyPair(): KeyPair? {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
                load(null)
            }

            if (keyStore.containsAlias(KEY_ALIAS)) {
                return null
            }

            val keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC,
                ANDROID_KEYSTORE
            )

            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_SIGN
            )
                .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            // Curve used in Web3
            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .build()

            keyPairGenerator.initialize(keyGenParameterSpec)


            return keyPairGenerator.generateKeyPair()
        }
    }
}
