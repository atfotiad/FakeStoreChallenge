package com.atfotiad.fakestorechallenge.data.repository

import com.atfotiad.fakestorechallenge.api.ApiService
import com.atfotiad.fakestorechallenge.model.LoginRequest
import com.atfotiad.fakestorechallenge.model.LoginResponse
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
}