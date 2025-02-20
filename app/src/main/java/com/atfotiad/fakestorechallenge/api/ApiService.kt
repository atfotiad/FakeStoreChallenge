package com.atfotiad.fakestorechallenge.api

import com.atfotiad.fakestorechallenge.model.LoginRequest
import com.atfotiad.fakestorechallenge.model.LoginResponse
import com.atfotiad.fakestorechallenge.model.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("/products/categories")
    suspend fun getCategories(): Response<List<String>>

    @GET("/products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): Response<List<Product>>

    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @PUT("/products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Response<Product>

    @PATCH("/products/{id}")
    suspend fun partiallyUpdateProduct(@Path("id") id: Int, @Body product: Product): Response<Product>

}