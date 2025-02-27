package com.atfotiad.fakestorechallenge.utils.repository

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 * @property Success is a data class that contains the data.
 * @property Error is a data class that contains the error.
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}