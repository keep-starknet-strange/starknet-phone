package com.snphone.snwalletsdk.utils

import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.snphone.snwalletsdk.MainActivity
import java.io.IOException
import java.security.GeneralSecurityException

class Keystore() {

    private val tag = "Keystore"
    private val context = MainActivity.applicationContext()
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