package com.atfotiad.fakestorechallenge.utils.repository

import android.content.Context
import com.atfotiad.fakestorechallenge.utils.repository.RepoUtils.getOrError
import com.atfotiad.fakestorechallenge.utils.repository.RepoUtils.toResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Response

/**
 *  [RepoUtils] is a object that contains the utils for the repository.
 *  @property toResult is an extension function that converts the response to a result.
 *  @property getOrError is an extension function that gets the data or shows an error dialog.
 * */
object RepoUtils {
    suspend fun <T : Any> (suspend () -> Response<T>).toResult(): Result<T> {
        return try {
            val response = invoke()
            response.toResult()
        } catch (t: Throwable) {
            Result.Error(Exception(t))
        }
    }

    fun <T : Any> Response<T>.toResult(): Result<T> {
        return if (isSuccessful) {
            val body = body()
            if (body != null) {
                Result.Success(body)
            } else {
                Result.Error(Exception("Response body is null"))
            }
        } else {
            Result.Error(
                Exception(
                    "Network request failed with code: ${code()} and message: ${(errorBody()!!.string())}"
                )
            )
        }
    }

    // Extension function to get the data or show an error dialog
    fun <T : Any> Result<T>.getOrError(context: Context): T? {
        return when (this) {
            is Result.Success -> data
            is Result.Error -> {
                try {
                    MaterialAlertDialogBuilder(context)
                        .setTitle("Api Service Error")
                        .setMessage(exception.message ?: "An unknown error occurred")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .create().apply {
                            setCanceledOnTouchOutside(false)
                            show()
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                null
            }
            is Result.Loading -> null
        }
    }
}