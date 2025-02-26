package com.atfotiad.fakestorechallenge.ui.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isUsernameValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
