package com.atfotiad.fakestorechallenge.security

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager
) {
    // (Keystore key alias)
    private val TOKEN_KEY = stringPreferencesKey("token")

    suspend fun saveToken(token: String) {
        val encryptedToken = cryptoManager.encrypt(token)
        dataStore.edit { preferences -> preferences[TOKEN_KEY] = encryptedToken }
    }

    suspend fun getToken(): String? {
        val preferences = dataStore.data.first()
        val encryptedToken = preferences[TOKEN_KEY] ?: return null
        return cryptoManager.decrypt(encryptedToken)
    }

    suspend fun hasToken(): Boolean {
        return getToken() != null
    }

    suspend fun deleteToken() {
        dataStore.edit { preferences -> preferences.remove(TOKEN_KEY) }
    }
}