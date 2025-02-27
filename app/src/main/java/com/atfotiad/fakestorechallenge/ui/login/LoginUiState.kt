package com.atfotiad.fakestorechallenge.ui.login
/**
 *  [LoginUiState] is a data class that contains the UI state for the login screen.
 *  @property username is a String that contains the username.
 *  @property password is a String that contains the password.
 *  @property isUsernameValid is a Boolean that indicates if the username is valid.
 *  @property isPasswordValid is a Boolean that indicates if the password is valid.
 *  @property isLoading is a Boolean that indicates if the login is loading.
 *  @property isSuccess is a Boolean that indicates if the login is successful.
 *  @property errorMessage is a String that contains the error message.
 * */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isUsernameValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
