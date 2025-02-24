package com.atfotiad.fakestorechallenge.usecase

import com.atfotiad.fakestorechallenge.data.model.LoginResponse
import com.atfotiad.fakestorechallenge.data.model.Product
import com.atfotiad.fakestorechallenge.utils.repository.Result

interface FakeStoreUseCase {
    suspend fun getCategories(): List<String>
    suspend fun getProductsByCategory(category: String): List<Product>
    suspend fun login(username: String, password: String): Result<LoginResponse>
    suspend fun updateProduct(product: Product): Boolean
    suspend fun partiallyUpdateProduct(product: Product): Boolean


}