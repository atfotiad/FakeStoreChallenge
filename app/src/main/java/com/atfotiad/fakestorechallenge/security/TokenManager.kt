package com.atfotiad.fakestorechallenge.security

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
/** Class that saves and retrieves login token from DataStore.
 *  [TokenManager] is a class that saves and retrieves login token from DataStore.
 *  @param dataStore is an instance of [DataStore]
 *  @param cryptoManager is an instance of [CryptoManager]
 * */
class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager
) {

    /** Companion object that contains constants for token key.
     * */
    companion object {
        val TOKEN_KEY = stringPreferencesKey("token_key")
    }

    /**
     *  [saveToken] is a suspend function that saves login token to DataStore.
     *  @param token is the login token.
     *  @return a [String] object that contains the encrypted token.
     * */
    suspend fun saveToken(token: String) {
        Log.d("TokenManager", "saveToken: Token before encryption: $token")
        val encryptedToken = cryptoManager.encrypt(token)
        Log.d("TokenManager", "saveToken: Token after encryption: $encryptedToken")
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = encryptedToken
        }
    }

    /**
     *  [getToken] is a function that returns a [Flow] of [String] objects.
     *  @return a [Flow] of [String] objects that contains the login token.
     * */
    fun getToken(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e("TokenManager", "getToken: IOException: ${exception.message}")
                    emit(emptyPreferences())
                } else {
                    Log.e("TokenManager", "getToken: Exception: ${exception.message}")
                    throw exception
                }
            }
            .map { preferences ->
                val encryptedToken = preferences[TOKEN_KEY]
                Log.d("TokenManager", "getToken: Encrypted token: $encryptedToken")
                val decryptedToken = encryptedToken?.let { cryptoManager.decrypt(it) }
                Log.d("TokenManager", "getToken: Decrypted token: $decryptedToken")
                decryptedToken
            }
    }

    /**
     *  [deleteToken] is a suspend function that deletes login token from DataStore.
     * */
    suspend fun deleteToken() {
        Log.d("TokenManager", "deleteToken: Deleting token")
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    /**
     *  [hasToken] is a suspend function that checks if login token exists in DataStore.
     *  @return a [Boolean] object that contains the result of the check.
     * */
    suspend fun hasToken(): Boolean {
        val encryptedToken = dataStore.data.firstOrNull()?.get(TOKEN_KEY)
        Log.d("TokenManager", "hasToken: Encrypted token: $encryptedToken")
        val hasToken = encryptedToken != null
        Log.d("TokenManager", "hasToken: hasToken: $hasToken")
        return hasToken
    }
}