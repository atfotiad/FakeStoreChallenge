package com.atfotiad.fakestorechallenge

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.core.app.ApplicationProvider
import com.atfotiad.fakestorechallenge.security.CryptoManager
import com.atfotiad.fakestorechallenge.security.TokenManager
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TokenManagerTest {

    private lateinit var tokenManager: TokenManager
    private lateinit var dataStore: DataStore<Preferences>

    @Mock
    lateinit var cryptoManager: CryptoManager

    private val testDispatcher = StandardTestDispatcher()
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "test_token_data_store")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dataStore = context.dataStore
        tokenManager = TokenManager(dataStore, cryptoManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        runTest {
            dataStore.edit { it.clear() }
        }
    }

    @Test
    fun saveToken_retrievesToken() = runTest {
        // Given
        val token = "testtoken"
        val encryptedToken = "encryptedtoken"
        whenever(cryptoManager.encrypt(token)).thenReturn(encryptedToken)
        whenever(cryptoManager.decrypt(encryptedToken)).thenReturn(token)

        // When
        tokenManager.saveToken(token)
        val retrievedToken = tokenManager.getToken().first()

        // Then
        assertThat(retrievedToken).isEqualTo(token)
    }

    @Test
    fun deleteToken_removesToken() = runTest {
        // Given
        val token = "testtoken"
        val encryptedToken = "encryptedtoken"
        whenever(cryptoManager.encrypt(token)).thenReturn(encryptedToken)
        whenever(cryptoManager.decrypt(encryptedToken)).thenReturn(token)
        tokenManager.saveToken(token)

        // When
        tokenManager.deleteToken()
        val retrievedToken = tokenManager.getToken().first()

        // Then
        assertThat(retrievedToken).isNull()
    }

    @Test
    fun hasToken_returnsTrue_whenTokenExists() = runTest {
        // Given
        val token = "testtoken"
        val encryptedToken = "encryptedtoken"
        whenever(cryptoManager.encrypt(token)).thenReturn(encryptedToken)
        whenever(cryptoManager.decrypt(encryptedToken)).thenReturn(token)
        tokenManager.saveToken(token)

        // When
        val hasToken = tokenManager.hasToken()

        // Then
        assertThat(hasToken).isTrue()
    }

    @Test
    fun hasToken_returnsFalse_whenTokenDoesNotExist() = runTest {
        // When
        val hasToken = tokenManager.hasToken()

        // Then
        assertThat(hasToken).isFalse()
    }

    @Test
    fun getToken_returnsNull_whenNoTokenSaved() = runTest {
        // When
        val retrievedToken = tokenManager.getToken().first()

        // Then
        assertThat(retrievedToken).isNull()
    }

    @Test
    fun getToken_returnsNull_whenDataStoreThrowsException() = runTest {
        // Given
        val token = "testtoken"
        val encryptedToken = "encryptedtoken"
        whenever(cryptoManager.encrypt(token)).thenReturn(encryptedToken)
        whenever(cryptoManager.decrypt(encryptedToken)).thenReturn(token)
        tokenManager.saveToken(token)
        // Simulate an exception by clearing the DataStore
        dataStore.edit { it.clear() }

        // When
        val retrievedToken = tokenManager.getToken().first()

        // Then
        assertThat(retrievedToken).isNull()
    }
}