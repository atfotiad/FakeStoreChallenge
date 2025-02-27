package com.atfotiad.fakestorechallenge.usecase

import com.atfotiad.fakestorechallenge.data.model.LoginRequest
import com.atfotiad.fakestorechallenge.data.repository.FakeStoreRepository
import com.atfotiad.fakestorechallenge.data.model.LoginResponse
import com.atfotiad.fakestorechallenge.data.model.product.Product
import com.atfotiad.fakestorechallenge.utils.repository.Result
import javax.inject.Inject
import javax.inject.Singleton

/**
 *  [FakeStoreUseCaseImpl] is an implementation of the [FakeStoreUseCase] interface.
 * */
@Singleton
class FakeStoreUseCaseImpl @Inject constructor(
    private val repository: FakeStoreRepository
) : FakeStoreUseCase{
    override suspend fun getCategories(): Result<List<String>> {
        return repository.getCategories()
    }

    override suspend fun getProductsByCategory(category: String): List<Product> {
        TODO("Not yet implemented")
    }

    override suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return repository.login(loginRequest.username, loginRequest.password)
    }

    override suspend fun updateProduct(product: Product): Result<Unit> {
        return repository.updateProduct(product.id, product)
    }

    override suspend fun partiallyUpdateProduct(product: Product): Result<Unit> {
      return repository.partiallyUpdateProduct(product.id, product)
    }

    override suspend fun getAllProducts(): Result<List<Product>> {
        return repository.getAllProducts()
    }

}