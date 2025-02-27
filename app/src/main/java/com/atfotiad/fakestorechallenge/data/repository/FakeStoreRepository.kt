package com.atfotiad.fakestorechallenge.data.repository

import com.atfotiad.fakestorechallenge.api.ApiService
import com.atfotiad.fakestorechallenge.data.model.LoginRequest
import com.atfotiad.fakestorechallenge.data.model.LoginResponse
import com.atfotiad.fakestorechallenge.data.model.product.Product
import com.atfotiad.fakestorechallenge.utils.repository.RepoUtils.toResult
import com.atfotiad.fakestorechallenge.utils.repository.Result
import javax.inject.Inject
/**
 *  [FakeStoreRepository] is a class that provides access to the FakeStore API.
 *  @property apiService is an instance of the [ApiService] interface.
 * */
class FakeStoreRepository @Inject constructor(
    private val apiService: ApiService
) {

    /** [login] is a suspend function that logs in a user.
     * @param username is the username of the user.
     * @param password is the password of the user.
     * @return a [Result] object that holds a [LoginResponse] or an [Exception].
     * */
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        val body = LoginRequest(username, password)
        return suspend { apiService.login(body) }.toResult()
    }

    /** [getAllProducts] is a suspend function that returns a list of all products.
     * @return a [Result] object that holds a list of [Product] or an [Exception].
     * */
    suspend fun getAllProducts(): Result<List<Product>> {
        return suspend { apiService.getAllProducts() }.toResult()
    }

    /** [getCategories] is a suspend function that returns a list of categories.
     * @return a [Result] object that holds a list of [String] or an [Exception].
     */
    suspend fun getCategories(): Result<List<String>> {
        return suspend { apiService.getCategories() }.toResult()
    }

    //not in use
    /** [getProductsByCategory] is a suspend function that returns a list of products by category.
     * @param category is the category of the products.
     * @return a [Result] object that holds a list of [Product] or an [Exception].
     * */
    suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return suspend { apiService.getProductsByCategory(category) }.toResult()
    }

    /**
     *  [updateProduct] is a suspend function that updates a product.
     *  return a [Result] object that holds a [Unit] or an [Exception].
     * */
    suspend fun updateProduct(id: Int, product: Product): Result<Unit> {
        return suspend { apiService.updateProduct(id, product) }.toResult()
    }

    /**
     *  [partiallyUpdateProduct] is a suspend function that partially updates a product.
     *  return a [Result] object that holds a [Unit] or an [Exception].
     * */
    suspend fun partiallyUpdateProduct(id: Int, product: Product): Result<Unit> {
        return suspend { apiService.partiallyUpdateProduct(id, product) }.toResult()
    }
}