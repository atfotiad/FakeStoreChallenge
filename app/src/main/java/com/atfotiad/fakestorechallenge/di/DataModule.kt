package com.atfotiad.fakestorechallenge.di

import com.atfotiad.fakestorechallenge.api.ApiService
import com.atfotiad.fakestorechallenge.data.repository.FakeStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *  [DataModule] is a Dagger-hilt module that provides dependencies for the data layer.
 *  @property provideRepository is a Dagger-hilt provider function that provides an instance of [FakeStoreRepository].
 * */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRepository(
        apiService: ApiService
    ): FakeStoreRepository = FakeStoreRepository(apiService)

}