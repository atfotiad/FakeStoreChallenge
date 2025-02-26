package com.atfotiad.fakestorechallenge.data.repository

import com.atfotiad.fakestorechallenge.api.ApiService
import com.atfotiad.fakestorechallenge.data.model.LoginRequest
import com.atfotiad.fakestorechallenge.data.model.LoginResponse
import com.atfotiad.fakestorechallenge.data.model.product.Product
import com.atfotiad.fakestorechallenge.utils.repository.RepoUtils.toResult
import com.atfotiad.fakestorechallenge.utils.repository.Result
import javax.inject.Inject

class FakeStoreRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        val body = LoginRequest(username, password)
        return suspend { apiService.login(body) }.toResult()
    }

    suspend fun getAllProducts(): Result<List<Product>> {
        return suspend { apiService.getAllProducts() }.toResult()
    }

    suspend fun getCategories(): Result<List<String>> {
        return suspend { apiService.getCategories() }.toResult()
    }

    //not in use
    suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return suspend { apiService.getProductsByCategory(category) }.toResult()
    }

    suspend fun updateProduct(id: Int, product: Product): Result<Unit> {
        return suspend { apiService.updateProduct(id, product) }.toResult()
    }

    suspend fun partiallyUpdateProduct(id: Int, product: Product): Result<Unit> {
        return suspend { apiService.partiallyUpdateProduct(id, product) }.toResult()

    }
}