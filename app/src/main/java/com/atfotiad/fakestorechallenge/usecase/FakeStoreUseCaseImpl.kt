package com.atfotiad.fakestorechallenge.usecase

import com.atfotiad.fakestorechallenge.data.repository.FakeStoreRepository
import com.atfotiad.fakestorechallenge.data.model.LoginResponse
import com.atfotiad.fakestorechallenge.data.model.Product
import com.atfotiad.fakestorechallenge.utils.repository.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeStoreUseCaseImpl @Inject constructor(
    private val repository: FakeStoreRepository
) : FakeStoreUseCase{
    override suspend fun getCategories(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsByCategory(category: String): List<Product> {
        TODO("Not yet implemented")
    }

    override suspend fun login(username: String, password: String): Result<LoginResponse> {
        return repository.login(username, password)
    }

    override suspend fun updateProduct(product: Product): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun partiallyUpdateProduct(product: Product): Boolean {
        TODO("Not yet implemented")
    }

}