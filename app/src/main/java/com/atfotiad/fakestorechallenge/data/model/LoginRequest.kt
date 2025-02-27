package com.atfotiad.fakestorechallenge.data.model

/**
 *  [LoginRequest] is a data class that represents a login request.
 *  @property username is the username of the user.
 *  @property password is the password of the user.
 * */
data class LoginRequest(
    val username: String,
    val password: String
)
