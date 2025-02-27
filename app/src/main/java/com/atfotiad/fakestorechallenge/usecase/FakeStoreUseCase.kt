package com.atfotiad.fakestorechallenge.usecase

import com.atfotiad.fakestorechallenge.data.model.LoginRequest
import com.atfotiad.fakestorechallenge.data.model.LoginResponse
import com.atfotiad.fakestorechallenge.data.model.product.Product
import com.atfotiad.fakestorechallenge.utils.repository.Result

/**
 *  [FakeStoreUseCase] is an interface that contains the use cases for the FakeStore app.
 * */
interface FakeStoreUseCase {
    suspend fun getCategories(): Result<List<String>>
    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse>
    suspend fun updateProduct(product: Product): Result<Unit>
    suspend fun partiallyUpdateProduct(product: Product): Result<Unit>
    suspend fun getAllProducts(): Result<List<Product>>
}