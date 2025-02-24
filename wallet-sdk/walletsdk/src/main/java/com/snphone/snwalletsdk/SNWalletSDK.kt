package com.snphone.snwalletsdk

import android.content.Context
import com.snphone.snwalletsdk.utils.StarknetClient
import kotlinx.coroutines.coroutineScope

class SNWalletSDK(
    private val context: Context,
) {
    // System service methods

    private var starknet: StarknetClient = StarknetClient(context, "")

    /**
     * Wallet only supports one address as of right now, thus we only need to get the wallet address once
     */
    public suspend fun initialAddressRequest() {
        throw NotImplementedError("Initial address request not implemented")
    }

    public fun getAddress(): String {
        throw NotImplementedError("Get address not implemented")
    }

    public suspend fun sendTransaction(
    ): String = coroutineScope {
        throw NotImplementedError("Send transaction not implemented")
    }

    public suspend fun signMessage(message: String, type: String = "personal_sign"): String = coroutineScope {
        throw NotImplementedError("Sign message not implemented")
    }


    public suspend fun getChainId(): Int = coroutineScope {
        throw NotImplementedError("Get chain id not implemented")
    }


}
