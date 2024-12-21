package com.example.walletapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreModule(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("wallet_prefs")
        private val HAS_ACCOUNT = booleanPreferencesKey("has_account")
        private val ACCOUNT_NAME = stringPreferencesKey("acc_name")
        private val PRIVATE_KEY = stringPreferencesKey("private_key")
    }

    val hasAccount: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[HAS_ACCOUNT] ?: false
    }

    suspend fun setHasAccount(hasAccount: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[HAS_ACCOUNT] = hasAccount
        }
    }
    val accName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[ACCOUNT_NAME] ?: ""
    }

    suspend fun setAccountName(accName: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_NAME] = accName
        }
    }
    val privateKey: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PRIVATE_KEY] ?: ""
    }

    suspend fun setPrivateKey(privateKey: String) {
        context.dataStore.edit { preferences ->
            preferences[PRIVATE_KEY] = privateKey
        }
    }
}