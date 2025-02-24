package com.atfotiad.fakestorechallenge.di

import com.atfotiad.fakestorechallenge.api.ApiService
import com.atfotiad.fakestorechallenge.data.repository.FakeStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRepository(
        apiService: ApiService
    ): FakeStoreRepository = FakeStoreRepository(apiService)

}