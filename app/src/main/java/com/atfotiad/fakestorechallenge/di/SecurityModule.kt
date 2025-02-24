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

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun provideCryptoManager(@ApplicationContext context: Context): CryptoManager {
        return CryptoManager(context)
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        @ApplicationContext context: Context,
        dataStore: DataStore<Preferences>,
        cryptoManager: CryptoManager
    ): TokenManager {
        return TokenManager(context, dataStore, cryptoManager)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}
