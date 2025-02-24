package com.atfotiad.fakestorechallenge.di

import com.atfotiad.fakestorechallenge.usecase.FakeStoreUseCase
import com.atfotiad.fakestorechallenge.usecase.FakeStoreUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindFakeStoreUseCase(
        useCaseImpl: FakeStoreUseCaseImpl
    ): FakeStoreUseCase
}