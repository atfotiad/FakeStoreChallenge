package com.atfotiad.fakestorechallenge.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.atfotiad.fakestorechallenge.security.CryptoManager
import com.atfotiad.fakestorechallenge.security.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Defines a Datastore instance using the preferencesDataStore delegate.
 * Extension property on Context to get the Datastore instance easily
 * from any Context.
 * */
private val Context.dataStore:
        DataStore<Preferences> by preferencesDataStore(
    name = "token_data_store"
)

/**
 *  [SecurityModule] is a Dagger-hilt module that provides dependencies for the security layer.
 *  @property provideCryptoManager is a Dagger-hilt provider function that provides an instance of [CryptoManager].
 *  @property provideTokenManager is a Dagger-hilt provider function that provides an instance of [TokenManager].
 *  @property provideDataStore is a Dagger-hilt provider function that provides an instance of [DataStore].
 * */
@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager {
        return CryptoManager()
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        dataStore: DataStore<Preferences>,
        cryptoManager: CryptoManager
    ): TokenManager {
        return TokenManager(dataStore, cryptoManager)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}
