package com.snphone.snwalletsdk.utils

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.swmansion.starknet.account.StandardAccount
import java.io.IOException
import java.security.GeneralSecurityException
import com.swmansion.starknet.data.types.Felt
import java.math.BigInteger
import com.swmansion.starknet.extensions.toFelt

class Signer(private val context: Context) {

    private val tag = "Keystore"

    fun generatePrivateKey(): Felt {
       // NOTE: this is just generating a private key that we a re storing. This should be updated to
        // use a keystore signer

        val randomPrivateKey = StandardAccount.generatePrivateKey()
        this.storeData(randomPrivateKey.value.toString())       // save the key generated
        val data = this.retrieveData()                          // retrieve it to generate public key
        val privateKey = BigInteger(data).toFelt

        return privateKey
    }

    private fun storeData(message: String) {
        // NOTE: this should be phased out as we transition to a built in TEE signer

        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "my_encrypted_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            val editor = sharedPreferences.edit()
            editor.putString("key", message)
            editor.apply()

        } catch (e: GeneralSecurityException) {
            Log.e(tag, "Security exception while storing data: ${e.message}", e)
        } catch (e: IOException) {
            Log.e(tag, "I/O exception while storing data: ${e.message}", e)
        }
    }

    private fun retrieveData(): String {
        // NOTE: this should be phased out as we transition to a built in TEE signer

        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "my_encrypted_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            return sharedPreferences.getString("key", "") ?: ""

        } catch (e: GeneralSecurityException) {
            Log.e(tag, "Security exception while retrieving data: ${e.message}", e)
            return ""
        } catch (e: IOException) {
            Log.e(tag, "I/O exception while retrieving data: ${e.message}", e)
            return ""
        }
    }
}