package com.example.walletapp.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.walletapp.ui.MainActivity
import java.io.IOException
import java.security.GeneralSecurityException
import javax.crypto.KeyGenerator

class Keystore() {

    private val tag = "Keystore"
    val context = MainActivity.applicationContext()
    fun storeData(message: String) {
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

    fun retrieveData(): String {
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

fun generateKey() {
    val keyGenerator = KeyGenerator.getInstance(
        KeyProperties.KEY_ALGORITHM_AES,
        "AndroidKeyStore"
    )
    val keyGenParameterSpec = KeyGenParameterSpec.Builder(
        "PIN_KEY",  // Key alias
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    ).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        .setUserAuthenticationRequired(false) // Set true if biometrics are required
        .build()

    keyGenerator.init(keyGenParameterSpec)
    keyGenerator.generateKey()
}

